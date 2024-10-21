package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.entity.Hotel;
import com.hotel.lodgingCommander.model.hotel.HotelResponseModel;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRepositoryCustom {

    List<HotelResponseModel> findAvailableHotels(
            String location,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int guests,
            int rooms
    );

    List<HotelResponseModel> findByLocation(String location);

    List<HotelResponseModel> findByRecentlyList(Pageable pageable);

    List<String> findNamesByIds(List<Long> ids);

    HotelResponseModel findByIdCustom(Long id);

}
