package com.progress.tracking.rest.service;

import com.progress.tracking.rest.dto.CourseDTO;
import com.progress.tracking.rest.mapper.CourseMapper;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.util.exception.CourseNotFoundException;
import com.progress.tracking.wrapper.udemy.UdemyApiWrapper;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseSearch;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class for searching courses on different platforms.
 */
@Service
public class CourseSearchService {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * Searches for courses on Udemy platform.
     *
     * @param req The request containing search parameters.
     * @return {@linkplain SearchCourseResponse} containing search results.
     */
    public SearchCourseResponse searchFromUdemy(final SearchCourseRequest req) {
        final SearchCourseResponse response = new SearchCourseResponse();
        final UdemyApiWrapper uWrapper = UdemyApiWrapper.initialize(req.getUdemyClientId(), req.getUdemyClientSecret());
        final UdemyCourseSearch ret = uWrapper.searchCourse(req.getCourse(), req.getMax(), req.getPage());

        if (ret.getCourses().isEmpty())
            throw new CourseNotFoundException(req.getCourse(), "Udemy");

        for (UdemyCourse udemyCourse : ret.getCourses()) {
            final Map<Result, List<Result>> chapters = uWrapper.getCourseCurriculum(udemyCourse.getId(), 100);
            final CourseDTO course = this.courseMapper.courseFromUdemy(udemyCourse, chapters);

            if (course == null)
                continue;

            response.getCourses().add(course);
        }
        response.setCurrentPage(ret.getCurrentPage());
        response.setNextPage(ret.getNextPage());
        response.setCount(ret.getCount());
        return response;
    }
}
