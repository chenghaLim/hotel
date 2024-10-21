package com.hotel.lodgingCommander.service.impl;

import com.hotel.lodgingCommander.model.FacilityModel;
import com.hotel.lodgingCommander.model.entity.Facility;
import com.hotel.lodgingCommander.model.repository.FacilityRepository;
import com.hotel.lodgingCommander.model.repository.HotelRepository;
import com.hotel.lodgingCommander.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private final HotelRepository hotelRepository;
    private final FacilityRepository facilityRepository;

    @Transactional
    public Boolean save(FacilityModel facilityDTO, Long hotelId) {
        facilityDTO.setHotelId(hotelId);
        var hotel = hotelRepository.findById(facilityDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        var facility = Facility.builder()
                .hotel(hotel)
                .freeWifi(facilityDTO.getFreeWifi())
                .nonSmoking(facilityDTO.getNonSmoking())
                .airConditioning(facilityDTO.getAirConditioning())
                .laundryFacilities(facilityDTO.getLaundryFacilities())
                .freeParking(facilityDTO.getFreeParking())
                .twentyFourHourFrontDesk(facilityDTO.getTwentyFourHourFrontDesk())
                .breakfast(facilityDTO.getBreakfast())
                .airportShuttle(facilityDTO.getAirportShuttle())
                .spa(facilityDTO.getSpa())
                .bar(facilityDTO.getBar())
                .swimmingPool(facilityDTO.getSwimmingPool())
                .gym(facilityDTO.getGym())
                .evChargingStation(facilityDTO.getEvChargingStation())
                .petFriendly(facilityDTO.getPetFriendly())
                .restaurant(facilityDTO.getRestaurant())
                .build();

        return facilityRepository.save(facility) != null ? true : false;
    }
}
