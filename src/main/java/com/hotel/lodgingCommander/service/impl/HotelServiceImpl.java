package com.hotel.lodgingCommander.service.impl;

import com.hotel.lodgingCommander.model.entity.Hotel;
import com.hotel.lodgingCommander.model.hotel.HotelRequestModel;
import com.hotel.lodgingCommander.model.hotel.HotelResponseModel;
import com.hotel.lodgingCommander.model.repository.AddressRepository;
import com.hotel.lodgingCommander.model.repository.CategoryRepository;
import com.hotel.lodgingCommander.model.repository.HotelRepository;
import com.hotel.lodgingCommander.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final UserServiceImpl userService;

    public HotelResponseModel getById(Long id) {
        return hotelRepository.findByIdCustom(id);
    }


    @Transactional(readOnly = true)
    public List<?> findAvailable(String location, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms) {
        int adjustedGuests = (guests % rooms == 0)
                ? guests / rooms
                : guests / rooms + 1;
        return hotelRepository.findAvailableHotels(location, checkInDate, checkOutDate, adjustedGuests, rooms);
    }

    public Map<?, ?> getAddressByHotelId(Long hotelId) {

        var address = addressRepository.findByHotelId(hotelId);
        var response = new HashMap<>();
        response.put("id", address);
        response.put("latitude", address.getLatitude());
        response.put("longitude", address.getLongitude());

        return response;
    }

    public List<?> findByLocation(String location) {
        return hotelRepository.findByLocation(location);
    }

    public List<?> getRecent() {
        Pageable pageable = PageRequest.of(0, 10);
        return hotelRepository.findByRecentlyList(pageable);
    }

    @Transactional
    public Boolean save(HotelRequestModel hotelDTO) {
        var address = addressRepository.findById(hotelDTO.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        var category = categoryRepository.findById(hotelDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        var user = userService.getUserById(hotelDTO.getUserId());
        var hotel = Hotel.builder()
                .name(hotelDTO.getName())
                .address(address)
                .category(category)
                .tel(hotelDTO.getTel())
                .grade(hotelDTO.getGrade())
                .detail(hotelDTO.getDetail())
                .user(user)
                .build();

        return hotelRepository.save(hotel) != null ? true : false;
    }

    @Transactional
    public List<?> getByUserId(Long userId) {
        return hotelRepository.findByUserId(userId).stream()
                .map(hotel -> new HotelRequestModel(
                        hotel.getId(),
                        hotel.getName(),
                        userId,
                        hotel.getAddress().getId(),
                        hotel.getCategory().getId(),
                        hotel.getTel(),
                        hotel.getGrade(),
                        hotel.getDetail()))
                .collect(Collectors.toList());
    }

    public List<String> getHotelNamesByIds(List<Long> ids) {
        return hotelRepository.findNamesByIds(ids);
    }

    @Override
    public List<HotelResponseModel> getAll() {
        return hotelRepository.findAll().stream()
                .map(hotel -> HotelResponseModel.builder()
                        .id(hotel.getId())
                        .hotelName(hotel.getName())
                        .grade(hotel.getGrade())
                        .detail(hotel.getDetail())
                        .category(hotel.getCategory().getName())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public Boolean delete(Long id) {
        hotelRepository.delete(hotelRepository.findById(id).get());
        return hotelRepository.findById(id) == null ? true : false;
    }
}
