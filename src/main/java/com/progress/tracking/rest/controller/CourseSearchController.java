package com.progress.tracking.rest.controller;

import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.rest.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for course search.
 */
@RestController
@RequestMapping("/search")
public class CourseSearchController {

    @Autowired
    private CourseSearchService courseSearch;

    /**
     * Endpoint to search for courses on Udemy platform.
     *
     * @param udemyClientId     The client ID for Udemy API.
     * @param udemyClientSecret The client secret for Udemy API.
     * @param course            The course name or keyword to search for.
     * @param page              The page number for pagination.
     * @param max               The maximum number of results per page.
     * @return {@link SearchCourseResponse} containing search results.
     */
    @GetMapping("/fromUdemy")
    public ResponseEntity<SearchCourseResponse> fromUdemy(
            @RequestHeader("Udemy-Client-Id") String udemyClientId,
            @RequestHeader("Udemy-Client-Secret") String udemyClientSecret,
            @RequestParam String course,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int max) {

        var request = SearchCourseRequest.builder()
                .udemyClientId(udemyClientId)
                .udemyClientSecret(udemyClientSecret)
                .course(course)
                .max(max)
                .page(page)
                .build();

        return ResponseEntity.ok(this.courseSearch.searchFromUdemy(request));
    }

}
