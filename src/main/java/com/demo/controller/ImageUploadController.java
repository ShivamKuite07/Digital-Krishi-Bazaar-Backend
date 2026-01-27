package com.demo.controller;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.demo.config.FileStorageConfig;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = "img_" + System.currentTimeMillis() + extension;

        Path userDir =
                FileStorageConfig.PRODUCT_IMAGE_ROOT.resolve(userId.toString());

        Files.createDirectories(userDir);

        Path filePath = userDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

        // 🔥 Return RELATIVE path
        return ResponseEntity.ok(
                "productImages/" + userId + "/" + fileName
        );
    }
}
