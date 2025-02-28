package com.hotel.lodgingCommander.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImgService {

    Boolean save(MultipartFile image,Long roomId);

    byte[] getImage(String path) throws IOException;

    Boolean delete(String path);
}
