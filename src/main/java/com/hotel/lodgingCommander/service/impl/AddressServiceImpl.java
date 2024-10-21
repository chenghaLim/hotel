package com.hotel.lodgingCommander.service.impl;


import com.hotel.lodgingCommander.model.AddressModel;
import com.hotel.lodgingCommander.model.entity.Addresses;
import com.hotel.lodgingCommander.model.repository.AddressRepository;
import com.hotel.lodgingCommander.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public String save(AddressModel addressDTO) {
        var address = Addresses.builder()
                .address(addressDTO.getAddress())
                .addressDetail(addressDTO.getAddressDetail())
                .postCode(addressDTO.getPostCode())
                .latitude(addressDTO.getLatitude())
                .longitude(addressDTO.getLongitude())
                .build();
        var savedAddress = addressRepository.save(address);
        return savedAddress != null ? savedAddress.getId().toString() : null;
    }
}
