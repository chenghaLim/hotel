package com.hotel.lodgingCommander.service;

import com.hotel.lodgingCommander.model.ReviewModel;

import java.util.List;

public interface ReviewService {

    Boolean save(ReviewModel reviewDTO);

    List<?> getByUserId(Long userId);

    List<?> getByHotelId(Long HotelId);

    ReviewModel getById(Long id);

    Boolean update(Long id, ReviewModel reviewDTO);

    Boolean delete(Long id);
}