package com.progress.tracking.rest.controller;

import com.progress.tracking.rest.dto.Platform;
import com.progress.tracking.rest.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping("/search")
    ResponseEntity<List<Platform.Info>> availableSearchPlatforms() {
        return ResponseEntity.ok(this.platformService.getAvailableSearchPlatforms());
    }

    @GetMapping("/submission")
    ResponseEntity<List<Platform.Info>> availableSubmissionPlatforms() {
        return ResponseEntity.ok(this.platformService.getAvailableSubmissionPlatforms());
    }

    @GetMapping("/info/{platform}")
    ResponseEntity<List<Platform.Field>> fieldsForPlatform(@PathVariable String platform) {
        return ResponseEntity.ok(this.platformService.getFieldsForPlatform(platform));
    }


}
