package com.hotel.lodgingCommander.model.repository;

import java.util.List;

public interface ImgRepositoryCustom {

    List<String> findPathListByHotelId(Long hotelId);
}
