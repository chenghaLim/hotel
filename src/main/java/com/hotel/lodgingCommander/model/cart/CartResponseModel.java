package com.hotel.lodgingCommander.model.cart;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseModel {
    private Long id;
    private String hotelName;
    private String roomName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int price;
    private String imgPath;
    private Long hotelId;
    private int grade;
    private String userGrade;
    private Boolean isAvailable;
    private int reviewCount;
    private Long roomId;
}
