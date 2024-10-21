package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {

    Addresses findByHotelId(Long hotelId);
}
