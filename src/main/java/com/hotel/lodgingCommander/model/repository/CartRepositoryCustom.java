package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.cart.CartResponseModel;
import com.hotel.lodgingCommander.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CartRepositoryCustom{

    List<CartResponseModel> findCartWithAvailabilityByUserId(Long userId);
}
