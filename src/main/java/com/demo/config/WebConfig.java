package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path imageDir = FileStorageConfig.PRODUCT_IMAGE_ROOT.toAbsolutePath();

        registry.addResourceHandler("/productImages/**")
                .addResourceLocations("file:" + imageDir + "/");
    }
}
