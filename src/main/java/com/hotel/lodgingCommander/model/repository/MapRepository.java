package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Addresses, Long> {
}
