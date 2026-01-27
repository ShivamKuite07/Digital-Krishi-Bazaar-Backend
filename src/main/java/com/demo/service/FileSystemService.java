package com.demo.service;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.stereotype.Service;
import com.demo.config.FileStorageConfig;

@Service
public class FileSystemService {

    public void ensureUserProductFolderExists(Integer userId) {
        try {
            Path userDir =
                FileStorageConfig.PRODUCT_IMAGE_ROOT.resolve(userId.toString());

            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create image directory", e);
        }
    }
}
