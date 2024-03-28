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

/**
 * Endpoints for course submission.
 */
@RestController
@RequestMapping("/submit")
public class CourseSubmissionController {

    @Autowired
    private CourseSubmissionService courseSubmission;

    /**
     * Endpoint to submit a course to Trello.
     *
     * @param req The {@linkplain CourseToTrelloRequest} containing the details of the course to be submitted.
     * @return The {@linkplain CourseToTrelloResponse} containing information about the submission status.
     */
    @PostMapping("/toTrello")
    public ResponseEntity<CourseToTrelloResponse> toTrello(@RequestBody CourseToTrelloRequest req) {
        return this.courseSubmission.submitToTrello(req);
    }
}
