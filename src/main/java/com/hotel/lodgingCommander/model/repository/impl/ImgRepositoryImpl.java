package com.hotel.lodgingCommander.model.repository.impl;

import com.hotel.lodgingCommander.model.repository.ImgRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.hotel.lodgingCommander.model.entity.QImg.img;

@RequiredArgsConstructor
public class ImgRepositoryImpl implements ImgRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<String> findPathListByHotelId(Long hotelId) {
        return queryFactory
                .select(img.path)
                .from(img)
                .where(img.room.hotel.id.eq(hotelId))
                .distinct()
                .fetch();
    }


}
