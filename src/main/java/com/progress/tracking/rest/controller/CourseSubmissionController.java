package com.progress.tracking.rest.controller;

import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.rest.service.CourseSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submit")
public class CourseSubmissionController {

    @Autowired
    private CourseSubmissionService courseSubmission;

    @PostMapping("/toTrello")
    public ResponseEntity<CourseToTrelloResponse> toTrello(@RequestBody CourseToTrelloRequest req) {
        return this.courseSubmission.submitToTrello(req);
    }
}
