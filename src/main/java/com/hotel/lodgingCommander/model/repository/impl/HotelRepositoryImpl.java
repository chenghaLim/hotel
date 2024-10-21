package com.hotel.lodgingCommander.model.repository.impl;

import com.hotel.lodgingCommander.model.FacilityModel;
import com.hotel.lodgingCommander.model.hotel.HotelResponseModel;
import com.hotel.lodgingCommander.model.repository.HotelRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.hotel.lodgingCommander.model.entity.QAddresses.addresses;
import static com.hotel.lodgingCommander.model.entity.QBookingList.bookingList;
import static com.hotel.lodgingCommander.model.entity.QFacility.facility;
import static com.hotel.lodgingCommander.model.entity.QHotel.hotel;
import static com.hotel.lodgingCommander.model.entity.QImg.img;
import static com.hotel.lodgingCommander.model.entity.QReview.review;
import static com.hotel.lodgingCommander.model.entity.QRoom.room;

@RequiredArgsConstructor
public class HotelRepositoryImpl implements HotelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public List<HotelResponseModel> findAvailableHotels(
            String location,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int guests,
            int rooms
    ) {
        var isAvailable =
                new CaseBuilder()
                        .when(
                                room.quantity.subtract(
                                        JPAExpressions
                                                .select(bookingList.count())
                                                .from(bookingList)
                                                .innerJoin(bookingList.room, room)
                                                .where(
                                                        room.hotel.eq(hotel)
                                                                .and(bookingList.cancel.isFalse())
                                                                .and(bookingList.checkInDate.loe(checkOutDate))
                                                                .and(bookingList.checkOutDate.goe(checkInDate))
                                                )
                                ).gt(rooms)
                        )
                        .then(1)
                        .otherwise(0);


        var ids = queryFactory
                .select(hotel.id)
                .from(hotel)
                .where(addresses.address.containsIgnoreCase(location)
                        .and(room.maxPeople.goe(guests))
                        .and(isAvailable.eq(1)))
                .innerJoin(hotel.rooms, room)
                .innerJoin(hotel.address, addresses)
                .fetch();

        return queryFactory
                .select(Projections.constructor(
                        HotelResponseModel.class,
                        hotel.id,
                        hotel.name.as("hotelName"),
                        hotel.grade.as("grade"),
                        hotel.detail.as("detail"),
                        img.path.as("imgPath"),
                        room.name.as("roomName"),
                        JPAExpressions
                                .select(room.price.min())
                                .from(room)
                                .where(room.hotel.eq(hotel)),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .where(review.hotel.eq(hotel)),
                        hotel.category.name,
                        Projections.constructor(
                                FacilityModel.class,
                                facility.airConditioning,
                                facility.airportShuttle,
                                facility.bar,
                                facility.breakfast,
                                facility.evChargingStation,
                                facility.freeParking,
                                facility.gym,
                                facility.laundryFacilities,
                                facility.nonSmoking,
                                facility.petFriendly,
                                facility.restaurant,
                                facility.spa,
                                facility.swimmingPool,
                                facility.twentyFourHourFrontDesk
                        )
                ))
                .from(hotel)
                .leftJoin(hotel.rooms, room)
                .leftJoin(room.img, img)
                .leftJoin(hotel.facility, facility)
                .where(hotel.id.in(ids))
                .fetch();
    }

    public List<HotelResponseModel> findByLocation(String location) {
        var ids = queryFactory
                .select(hotel.id)
                .from(hotel)
                .innerJoin(hotel.address, addresses)
                .where(addresses.address.startsWith(location))
                .orderBy(hotel.id.desc())
                .fetch();

        return queryFactory
                .select(Projections.constructor(
                        HotelResponseModel.class,
                        hotel.id,
                        hotel.name.as("hotelName"),
                        hotel.grade.as("grade"),
                        hotel.detail.as("detail"),
                        img.path.as("imgPath"),
                        room.name.as("roomName"),
                        JPAExpressions
                                .select(room.price.min())
                                .from(room)
                                .where(room.hotel.eq(hotel)),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .where(review.hotel.eq(hotel)),
                        hotel.category.name,
                        Projections.constructor(
                                FacilityModel.class,
                                facility.airConditioning,
                                facility.airportShuttle,
                                facility.bar,
                                facility.breakfast,
                                facility.evChargingStation,
                                facility.freeParking,
                                facility.gym,
                                facility.laundryFacilities,
                                facility.nonSmoking,
                                facility.petFriendly,
                                facility.restaurant,
                                facility.spa,
                                facility.swimmingPool,
                                facility.twentyFourHourFrontDesk
                        )
                ))
                .from(hotel)
                .leftJoin(hotel.rooms, room)
                .leftJoin(room.img, img)
                .leftJoin(hotel.facility, facility)
                .where(hotel.id.in(ids))
                .fetch();
    }

    public List<HotelResponseModel> findByRecentlyList(Pageable pageable) {
        var ids = queryFactory
                .select(hotel.id)
                .from(hotel)
                .orderBy(hotel.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(hotel.id.desc())
                .fetch();

        return queryFactory
                .select(Projections.constructor(
                        HotelResponseModel.class,
                        hotel.id,
                        hotel.name.as("hotelName"),
                        hotel.grade.as("grade"),
                        hotel.detail.as("detail"),
                        img.path.as("imgPath"),
                        room.name.as("roomName"),
                        JPAExpressions
                                .select(room.price.min())
                                .from(room)
                                .where(room.hotel.eq(hotel)),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .where(review.hotel.eq(hotel)),
                        hotel.category.name,
                        Projections.constructor(
                                FacilityModel.class,
                                facility.airConditioning,
                                facility.airportShuttle,
                                facility.bar,
                                facility.breakfast,
                                facility.evChargingStation,
                                facility.freeParking,
                                facility.gym,
                                facility.laundryFacilities,
                                facility.nonSmoking,
                                facility.petFriendly,
                                facility.restaurant,
                                facility.spa,
                                facility.swimmingPool,
                                facility.twentyFourHourFrontDesk
                        )
                ))
                .from(hotel)
                .leftJoin(hotel.rooms, room)
                .leftJoin(room.img, img)
                .leftJoin(hotel.facility, facility)
                .where(hotel.id.in(ids))
                .fetch();
    }

    public List<String> findNamesByIds(List<Long> ids) {
        return queryFactory
                .select(hotel.name)
                .from(hotel)
                .where(hotel.id.in(ids))
                .fetch();
    }

    @Override
    public HotelResponseModel findByIdCustom(Long id) {
        return queryFactory
                .select(Projections.constructor(
                        HotelResponseModel.class,
                        hotel.id,
                        hotel.name.as("hotelName"),
                        hotel.grade.as("grade"),
                        hotel.detail.as("detail"),
                        img.path.as("imgPath"),
                        room.name.as("roomName"),
                        JPAExpressions
                                .select(room.price.min())
                                .from(room)
                                .where(room.hotel.eq(hotel)),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .where(review.hotel.eq(hotel)),
                        hotel.category.name,
                        Projections.constructor(
                                FacilityModel.class,
                                facility.airConditioning,
                                facility.airportShuttle,
                                facility.bar,
                                facility.breakfast,
                                facility.evChargingStation,
                                facility.freeParking,
                                facility.gym,
                                facility.laundryFacilities,
                                facility.nonSmoking,
                                facility.petFriendly,
                                facility.restaurant,
                                facility.spa,
                                facility.swimmingPool,
                                facility.twentyFourHourFrontDesk
                        )
                ))
                .from(hotel)
                .leftJoin(hotel.rooms, room)
                .leftJoin(room.img, img)
                .leftJoin(hotel.facility, facility)
                .where(hotel.id.eq(id))
                .fetchOne();
    }
}
