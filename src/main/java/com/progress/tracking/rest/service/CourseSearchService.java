package com.progress.tracking.rest.service;

import com.progress.tracking.rest.mapper.CourseMapper;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service class for searching courses on different platforms.
 */
@Service
public class CourseSearchService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UdemyService udemyService;

    /**
     * Searches for courses on Udemy platform.
     *
     * @param req The request containing search parameters.
     */
    public SearchCourseResponse searchFromUdemy(final SearchCourseRequest req) {
        var ret = this.udemyService.searchCourse(req.getUdemyClientId(), req.getUdemyClientSecret(), req.getCourse(), req.getMax(), req.getPage());

        return SearchCourseResponse.builder()
                .currentPage(ret.getCurrentPage())
                .nextPage(ret.getNextPage())
                .count(ret.getCount())
                .previsouPage(ret.getPreviousPage())
                .courses(ret.getCourses().stream()
                        .map(udemyCourse -> {
                            var chapters = this.udemyService.getCourseCurriculum(req.getUdemyClientId(), req.getUdemyClientSecret(), udemyCourse.getId(), 100);
                            return this.courseMapper.courseFromUdemy(udemyCourse, chapters);
                        })
                        .filter(Objects::nonNull)
                        .toList())
                .build();
    }
}
