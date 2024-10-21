package com.hotel.lodgingCommander.service;

import com.hotel.lodgingCommander.model.LikeListModel;

import java.util.List;
import java.util.Map;

public interface LikeListService {

    Map<?, ?> save(LikeListModel likeListDTO);

    Boolean delete(LikeListModel likeListDTO);
    Boolean removeLikeById(Long id);


    List<?> getLikesByUserId(long userId) ;

    LikeListModel findByUserIdAndHotelId(Long userId, Long hotelId);
}