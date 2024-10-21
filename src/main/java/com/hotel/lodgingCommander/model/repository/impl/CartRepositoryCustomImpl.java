package com.hotel.lodgingCommander.model.repository.impl;

import com.hotel.lodgingCommander.model.cart.CartResponseModel;
import com.hotel.lodgingCommander.model.repository.CartRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.hotel.lodgingCommander.model.entity.QBookingList.bookingList;
import static com.hotel.lodgingCommander.model.entity.QCart.cart;
import static com.hotel.lodgingCommander.model.entity.QHotel.hotel;
import static com.hotel.lodgingCommander.model.entity.QImg.img;
import static com.hotel.lodgingCommander.model.entity.QRoom.room;
import static com.hotel.lodgingCommander.model.entity.QUser.user;
import static com.hotel.lodgingCommander.model.entity.QReview.review;

@RequiredArgsConstructor
public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CartResponseModel> findCartWithAvailabilityByUserId(Long userId) {
        // CASE 문: 방의 재고와 예약을 비교하여 isAvailable 계산
        NumberExpression<Integer> isAvailable =
                new CaseBuilder()
                        .when(
                                room.quantity.gt(
                                        queryFactory
                                                .select(bookingList.id.count())
                                                .from(bookingList)
                                                .where(
                                                        bookingList.room.eq(room)
                                                                .and(bookingList.cancel.isFalse())
                                                                .and(bookingList.checkInDate.loe(cart.checkOutDate))
                                                                .and(bookingList.checkOutDate.goe(cart.checkInDate))
                                                )
                                )
                        )
                        .then(1)
                        .otherwise(0);

        // QueryDSL 쿼리 실행: 필요한 데이터를 CartDTO로 매핑
        return queryFactory
                .select(Projections.constructor(
                        CartResponseModel.class,
                        cart.id,
                        hotel.name.as("hotelName"),
                        room.name.as("roomName"),
                        cart.checkInDate.as("checkInDate"),
                        cart.checkOutDate.as("checkOutDate"),
                        room.price.as("price"),
                        img.path.as("imgPath"),
                        hotel.id.as("hotelId"),
                        hotel.grade.as("grade"),
                        user.grade.as("UserGrade"),
                        isAvailable.as("isAvailable"),
                        hotel.reviews.size().as("reviewCount"),
                        room.id.as("roomId")
                        ))
                .from(cart)
                .innerJoin(cart.room, room)
                .innerJoin(room.hotel, hotel)
                .leftJoin(hotel.reviews, review)
                .leftJoin(room.img, img)
                .innerJoin(cart.user, user)
                .where(cart.user.id.eq(userId))
                .orderBy(cart.id.desc())
                .fetch();
    }
}
