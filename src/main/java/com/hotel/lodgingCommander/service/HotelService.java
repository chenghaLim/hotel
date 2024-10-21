package com.hotel.lodgingCommander.service;

import com.hotel.lodgingCommander.model.hotel.HotelRequestModel;
import com.hotel.lodgingCommander.model.hotel.HotelResponseModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HotelService {

    HotelResponseModel getById(Long id);


    List<?>  findAvailable(String location, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms);

    Map<?, ?> getAddressByHotelId(Long hotelId);

    List<?> findByLocation(String location);

    List<?> getRecent();

    Boolean save(HotelRequestModel hotelDTO);

    List<?> getByUserId(Long userId);

    List<String> getHotelNamesByIds(List<Long> ids);

    List<HotelResponseModel> getAll();

    Boolean delete(Long id);
}
