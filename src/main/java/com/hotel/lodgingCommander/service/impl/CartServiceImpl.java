package com.hotel.lodgingCommander.service.impl;

import com.hotel.lodgingCommander.model.cart.CartRequestModel;
import com.hotel.lodgingCommander.model.cart.CartResponseModel;
import com.hotel.lodgingCommander.model.entity.Cart;
import com.hotel.lodgingCommander.model.repository.CartRepository;
import com.hotel.lodgingCommander.model.repository.ReviewRepository;
import com.hotel.lodgingCommander.model.repository.RoomRepository;
import com.hotel.lodgingCommander.model.repository.UserRepository;
import com.hotel.lodgingCommander.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Map<?, ?> getCartsByUserId(Long userId) {
        var carts = cartRepository.findCartWithAvailabilityByUserId(userId);
        var response = new HashMap<>();
        response.put("cartList", carts);
        return response;
    }

    @Transactional
    public Boolean delete(Map<?, ?> request) {
        var response = new HashMap<>();
        try {
            var idNumber = (Number) request.get("id");
            if (idNumber != null) {
                Long id = idNumber.longValue();
                cartRepository.deleteById(id);
                return true;
            } else {
               return false;
            }
        } catch (Exception e) {
            // 예외 처리
            return false;
        }
    }

    @Transactional
    public Boolean save(CartRequestModel requestDTO) {
        var user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        var room = roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room Not Found"));
        var cartEntity = Cart.builder()
                .id(requestDTO.getId())
                .checkInDate(requestDTO.getCheckInDate())
                .checkOutDate(requestDTO.getCheckOutDate())
                .room(room)
                .user(user)
                .build();
        return cartRepository.save(cartEntity).getId() == null ? false : true;
    }
}
