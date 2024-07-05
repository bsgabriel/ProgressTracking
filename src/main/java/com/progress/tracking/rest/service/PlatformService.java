package com.progress.tracking.rest.service;

import com.progress.tracking.rest.config.PlatformConfiguration;
import com.progress.tracking.rest.entity.Platform;
import com.progress.tracking.util.exception.PlatformNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Platform.Field> getFieldsForPlatform(String platform) {
        return this.getAllPlatforms()
                .stream()
                .filter(p -> p.getInfo().getName().equals(platform))
                .findFirst()
                .orElseThrow(() -> new PlatformNotFoundException(platform))
                .getFields();

    }

    private List<Platform> getAllPlatforms() {
        final List<Platform> allPlatforms = new ArrayList<>();
        allPlatforms.addAll(this.platformConfiguration.getSearch());
        allPlatforms.addAll(this.platformConfiguration.getSubmission());
        return allPlatforms;
    }


}
