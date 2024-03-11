package com.progress.tracking.rest;

import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.rest.request.AbstractRequest;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperInitializationException;
import com.progress.tracking.wrapper.udemy.UdemyApiWrapper;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseSearch;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @PostMapping("searchCourse")
    public ResponseEntity<SearchCourseResponse> searchCourse(@RequestBody SearchCourseRequest req) {
        final UdemyApiWrapper uWrapper;
        final SearchCourseResponse response = new SearchCourseResponse();

        try {
            uWrapper = initUdemyWrapper(req);
        } catch (WrapperInitializationException e) {
            response.setError(e.getMessage());
            if (e.getCause() != null)
                response.setDetailedMessage(e.getCause().getMessage());

            return ResponseEntity.internalServerError().body(response);
        }

        final UdemyCourseSearch ret;
        try {
            final Integer page = req.getPage() != null ? req.getPage() : 1;
            ret = uWrapper.searchCourse(req.getCourse(), req.getMax(), page);
        } catch (InvalidParameterException e) {
            response.setError("Error while searching for course");
            response.setDetailedMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (ApiExecutionException e) {
            response.setError(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        if (ret.getCourses().isEmpty()) {
            response.setMessage("Couldn't find the specified course");
            return ResponseEntity.status(404).body(response);
        }

        for (UdemyCourse c : ret.getCourses()) {
            final Course courseInfo = new Course();
            courseInfo.setName(c.getTitle());
            courseInfo.setDesc(c.getHeadline());
            courseInfo.setImage(c.getImage());
            courseInfo.setUrl(c.getUrl());

            for (VisibleInstructor instructor : c.getInstructors())
                courseInfo.getInstructors().add(instructor.getDisplayName());

            response.getCourses().add(courseInfo);
        }

        response.setCurrentPage(ret.getCurrentPage());
        response.setPrevisouPage(ret.getPreviousPage());
        response.setNextPage(ret.getNextPage());
        return ResponseEntity.ok().body(response);
    }

    private static UdemyApiWrapper initUdemyWrapper(final AbstractRequest req) throws WrapperInitializationException {
        try {
            return UdemyApiWrapper.initialize(req.getUdemyClientId(), req.getUdemyClientSecret());
        } catch (InvalidParameterException e) {
            throw new WrapperInitializationException("Couldn't initialize Udemy's API wrapper", e);
        }
    }

}
