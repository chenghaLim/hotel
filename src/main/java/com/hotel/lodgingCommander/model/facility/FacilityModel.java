package com.hotel.lodgingCommander.model.facility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityModel {
    private Boolean airConditioning;
    private Boolean airportShuttle;
    private Boolean bar;
    private Boolean breakfast;
    private Boolean evChargingStation;
    private Boolean freeParking;
    private Boolean gym;
    private Boolean laundryFacilities;
    private Boolean nonSmoking;
    private Boolean petFriendly;
    private Boolean restaurant;
    private Boolean spa;
    private Boolean swimmingPool;
    private Boolean twentyFourHourFrontDesk;
}
