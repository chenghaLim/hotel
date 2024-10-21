package com.hotel.lodgingCommander.model.repository.impl;

import com.hotel.lodgingCommander.model.FacilityModel;
import com.hotel.lodgingCommander.model.repository.FacilityRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.hotel.lodgingCommander.model.entity.QFacility.facility;
import static com.hotel.lodgingCommander.model.entity.QHotel.hotel;
import static com.hotel.lodgingCommander.model.entity.QRoom.room;

@RequiredArgsConstructor
public class FacilityRepositoryCustomImpl implements FacilityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public FacilityModel findByRoomId(Long roomId) {
        return queryFactory
                .select(Projections.constructor(
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
                ))
                .from(room)
                .innerJoin(room.hotel, hotel)
                .innerJoin(hotel.facility, facility)
                .where(room.id.eq(roomId))
                .fetchOne();
    }
}
