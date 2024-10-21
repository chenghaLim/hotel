package com.hotel.lodgingCommander.model.repository.impl;

import com.hotel.lodgingCommander.model.entity.Room;
import com.hotel.lodgingCommander.model.repository.RoomRepositoryCustom;
import com.hotel.lodgingCommander.model.room.RoomResponseModel;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.hotel.lodgingCommander.model.entity.QBookingList.bookingList;
import static com.hotel.lodgingCommander.model.entity.QHotel.hotel;
import static com.hotel.lodgingCommander.model.entity.QImg.img;
import static com.hotel.lodgingCommander.model.entity.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Integer findMinPriceByHotelId(Long hotelId) {
        return queryFactory
                .selectDistinct(room.price.max())
                .from(room)
                .where(room.hotel.id.eq(hotelId))
                .fetchOne();
    }

    public List<RoomResponseModel> findRoomsWithBookingStatus(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        BooleanExpression dateOverlapCondition =
                bookingList.checkInDate.goe(checkInDate).and(bookingList.checkInDate.loe(checkOutDate))
                        .or(bookingList.checkOutDate.goe(checkInDate).and(bookingList.checkOutDate.loe(checkOutDate)))
                        .and(bookingList.cancel.isFalse());

        return queryFactory
                .select(Projections.constructor(
                        RoomResponseModel.class,
                        room.id,
                        room.name.as("roomName"),
                        room.maxPeople,
                        room.price,
                        room.detail,
                        // 예약된 방 수가 방의 수량과 같은 경우 예약 상태 반환
                        new CaseBuilder()
                                .when(room.quantity.gt(
                                        JPAExpressions.select(bookingList.count())
                                                .from(bookingList)
                                                .where(bookingList.room.eq(room).and(dateOverlapCondition))
                                )).then(true)
                                .otherwise(false).as("reservable"),
                        img.path.as("imgPath"),
                        hotel.name.as("hotelName"),
                        hotel.id.as("hotelId")
                ))
                .from(room)
                .innerJoin(room.hotel, hotel)
                .innerJoin(room.img, img)
                .leftJoin(bookingList).on(bookingList.room.eq(room).and(dateOverlapCondition))
                .where(hotel.id.eq(hotelId))
                .distinct()
                .fetch();
    }

    public Room findRoomById(Long id) {
        return queryFactory
                .selectDistinct(room)
                .from(room)
                .where(room.id.eq(id))
                .fetchOne();
    }

}
