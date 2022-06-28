package com.app.gamelibrarymanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class config implements WebMvcConfigurer {

    /**
     * This method is getting used to expose or new photo directory, where we are storing our product photos.
     * We are adding that directory in our resource.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry reg) {
        exposeDirectory("game-photos", reg);
    }

    private void exposeDirectory(String img, ResourceHandlerRegistry reg) {
        Path imgdir = Paths.get(img);
        String imgupload = imgdir.toFile().getAbsolutePath();

        if (img.startsWith("../")) img = img.replace("../", "");

        reg.addResourceHandler("/" + img + "/**").addResourceLocations("file:/" + imgupload + "/");
    }
}
