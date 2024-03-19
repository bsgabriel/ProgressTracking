package com.progress.tracking.rest.controller;

import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.rest.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for course search.
 */
@RestController()
@RequestMapping("/search")
public class CourseSearchController {

    @Autowired
    private CourseSearchService courseSearch;

    /**
     * Endpoint to search for courses on Udemy platform.
     *
     * @param req The {@linkplain SearchCourseRequest} containing search parameters.
     * @return ResponseEntity containing search results.
     */
    @PostMapping("/fromUdemy")
    public ResponseEntity<SearchCourseResponse> fromUdemy(@RequestBody SearchCourseRequest req) {
        return this.courseSearch.searchFromUdemy(req);
    }

}
