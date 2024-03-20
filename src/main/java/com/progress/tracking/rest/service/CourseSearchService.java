package com.progress.tracking.rest.service;

import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.rest.mapper.CourseMapper;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.wrapper.udemy.UdemyApiWrapper;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseCurriculum;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
     * @return ResponseEntity containing search results.
     */
    public ResponseEntity<SearchCourseResponse> searchFromUdemy(final SearchCourseRequest req) {
        final SearchCourseResponse response = new SearchCourseResponse();
        try {
            final UdemyApiWrapper uWrapper = UdemyApiWrapper.initialize(req.getUdemyClientId(), req.getUdemyClientSecret());
            final UdemyCourseSearch ret = uWrapper.searchCourse(req.getCourse(), req.getMax(), req.getPage());

            if (ret.getCourses().isEmpty()) {
                response.setMessage("Couldn't find the specified course.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            for (UdemyCourse udemyCourse : ret.getCourses()) {
                final UdemyCourseCurriculum courseCurriculum = uWrapper.getCourseCurriculum(udemyCourse.getId(), 100);
                final Course course = this.courseMapper.udemyCourseToCourse(udemyCourse, courseCurriculum);

                if (course == null)
                    continue;

                response.getCourses().add(course);
            }
        } catch (InvalidParameterException | ApiExecutionException e) {
            response.setError(e.getMessage());
            if (e instanceof InvalidParameterException)
                return ResponseEntity.badRequest().body(response);

            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok().body(response);
    }
}
