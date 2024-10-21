package com.hotel.lodgingCommander.controller;

import com.hotel.lodgingCommander.model.hotel.HotelRequestModel;
import com.hotel.lodgingCommander.service.impl.HotelServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping({"/hotels"})
public class HotelController {
    private final HotelServiceImpl service;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/map/{hotelId}")
    public ResponseEntity<?> getAddressByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(service.getAddressByHotelId(hotelId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSearchList(
            @RequestParam(name = "location") String location,
            @RequestParam(name = "checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(name = "checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(name = "guests") int guests,
            @RequestParam(name = "rooms") int rooms) {
        return ResponseEntity.ok(service.findAvailable(
                location,
                checkInDate,
                checkOutDate,
                guests,
                rooms
        ));
    }


    @GetMapping("/{location}")
    public ResponseEntity<?> getListByLocation(@PathVariable String location) {
        return ResponseEntity.ok(service.findByLocation(location));
    }

    @GetMapping({"/newHotels"})
    public ResponseEntity<?> getNewHotels() {
        return ResponseEntity.ok(service.getRecent());
    }

    @GetMapping("/names")
    public ResponseEntity<?> getHotelName(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(service.getHotelNamesByIds(ids));
    }

    @PostMapping("/hotel")
    public ResponseEntity<?> saveHotel(@RequestBody HotelRequestModel hotelDTO) {
        return ResponseEntity.ok(service.save(hotelDTO));
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<?>> getHotelsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteHotel(@RequestParam Long id){
        return ResponseEntity.ok(service.delete(id));
    }
}
