package com.hotel.lodgingCommander.model.repository;

import com.hotel.lodgingCommander.model.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long>,ImgRepositoryCustom {

    Img findByPath(String path);
}
