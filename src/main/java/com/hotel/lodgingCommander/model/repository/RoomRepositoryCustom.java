package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.entity.Room;
import com.hotel.lodgingCommander.model.room.RoomResponseModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepositoryCustom {

    Integer findMinPriceByHotelId(Long hotelId);

    List<RoomResponseModel> findRoomsWithBookingStatus(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate);

    Room findRoomById(Long id);

}
