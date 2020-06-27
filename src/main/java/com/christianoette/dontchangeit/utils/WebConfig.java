package com.christianoette.dontchangeit.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow cross origin requests from local javascript application
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
