package com.progress.tracking.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (allowedOrigins.length == 0)
            return;

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST");
    }
}
