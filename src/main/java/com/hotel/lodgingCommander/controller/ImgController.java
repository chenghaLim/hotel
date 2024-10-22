package com.hotel.lodgingCommander.controller;

import com.hotel.lodgingCommander.service.impl.ImgServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/imgs")
@RequiredArgsConstructor
public class ImgController {

    private final ImgServiceImpl service;

    @PostMapping
    public ResponseEntity<?> saveImage(MultipartFile image, Long roomId){
        return ResponseEntity.ok(service.save(image,roomId));
    }

    @GetMapping("")
    public ResponseEntity<?> getFile(@RequestParam("path") String path) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.getImage(path));
    }

    @DeleteMapping("/{path}")
    public ResponseEntity<?> delete(@PathVariable String path) {
        return ResponseEntity.ok(service.delete(path));
    }

}
