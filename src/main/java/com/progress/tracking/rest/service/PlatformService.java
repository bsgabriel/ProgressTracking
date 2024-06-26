package com.progress.tracking.rest.service;

import com.progress.tracking.rest.config.PlatformConfiguration;
import com.progress.tracking.rest.entity.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {

    @Autowired
    private PlatformConfiguration platformConfiguration;

    public List<Platform.Info> getAvailableSearchPlatforms() {
        return this.platformConfiguration.getSearch()
                .stream()
                .map(Platform::getInfo)
                .toList();
    }

    public List<Platform.Info> getAvailableSubmissionPlatforms() {
        return this.platformConfiguration.getSubmission()
                .stream()
                .map(Platform::getInfo)
                .toList();
    }

}
