package com.demo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageConfig {

    // Root folder (relative to project root)
    public static final Path PRODUCT_IMAGE_ROOT =
            Paths.get("productImages");
}
