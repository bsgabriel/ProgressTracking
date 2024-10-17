package com.progress.tracking.rest.config;

import com.progress.tracking.rest.dto.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "platforms")
@AllArgsConstructor
public class PlatformConfiguration {
    final private List<Platform> search;
    final private List<Platform> submission;
}
