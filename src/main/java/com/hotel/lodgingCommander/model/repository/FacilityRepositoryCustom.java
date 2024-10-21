package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.FacilityModel;

public interface FacilityRepositoryCustom {
    FacilityModel findByRoomId(Long roomId);
}
