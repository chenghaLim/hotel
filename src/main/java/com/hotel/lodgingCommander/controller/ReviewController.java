package com.hotel.lodgingCommander.controller;
import com.hotel.lodgingCommander.model.ReviewModel;
import com.hotel.lodgingCommander.service.ReviewService;
import com.hotel.lodgingCommander.service.impl.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReviewModel reviewDTO) {
        return ResponseEntity.ok(service.save(reviewDTO));
    }

    //userid받아서 리뷰 확인
    @GetMapping("/listByUserId")
    public ResponseEntity<?> getByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    //hotelid받아서 리뷰 확인
    @GetMapping("/findByHotel")
    public ResponseEntity<?> getByHotelId(@RequestParam Long hotelId) {
        return ResponseEntity.ok(service.getByHotelId(hotelId));
    }


    //id받아서 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReviewModel reviewDTO) {
        return ResponseEntity.ok(service.update(id, reviewDTO));
    }



}