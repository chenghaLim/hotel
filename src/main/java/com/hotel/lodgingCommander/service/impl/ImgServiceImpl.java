package com.hotel.lodgingCommander.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import com.hotel.lodgingCommander.model.entity.Img;
import com.hotel.lodgingCommander.model.repository.ImgRepository;
import com.hotel.lodgingCommander.model.repository.RoomRepository;
import com.hotel.lodgingCommander.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImgServiceImpl implements ImgService {

    private final ImgRepository imgRepository;
    private final RoomRepository roomRepository;

    @Value("${cloud.s3.bucket}")
    private String s3bucket;
    private final AmazonS3 amazonS3;

//    @Transactional
//    public Boolean save(MultipartFile image) throws IOException {
//
//        var uploadDir = "src/main/resources/static/uploads";
//        var uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        var fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//        var filePath = uploadPath.resolve(fileName);
//
//        Files.copy(image.getInputStream(), filePath);
//
//        var img = Img.builder()
//                .path("http://localhost:8080/uploads/" + fileName)
//                .build();
//        return imgRepository.save(img) != null ? true : false;
//    }


    @Override
    public Boolean save(MultipartFile image, Long roomId) {
        String fileName = Objects.requireNonNull(image.getOriginalFilename(), "파일 이름이 없습니다.");
        String uploadName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        try {
            uploadImageToStorage(image, uploadName);
            saveImageMetadata(uploadName, roomId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드에 실패했습니다: " + e.getMessage(), e);
        }
    }

    private PutObjectResult uploadImageToStorage(MultipartFile file, String uploadName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            return amazonS3.putObject(s3bucket, uploadName, file.getInputStream(), metadata);
        } catch (IOException | SdkClientException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private Img saveImageMetadata(String uploadName, Long roomId) {
        return Optional.of(Img.builder()
                        .path(uploadName)
                        .room(roomRepository.findRoomById(roomId))
                        .build())
                .map(Img -> imgRepository.save(Img))
                .orElseThrow(() -> new RuntimeException("Failed to save file metadata"));
    }

    @Override
    public byte[] getImage(String path) throws IOException {
        return IOUtils.toByteArray(amazonS3
                .getObject(s3bucket, path)
                .getObjectContent());
    }

    @Override
    public Boolean delete(String path) {
        amazonS3.deleteObject(s3bucket,path);
        imgRepository.delete(imgRepository.findByPath(path));
        return Boolean.TRUE;
    }
}
