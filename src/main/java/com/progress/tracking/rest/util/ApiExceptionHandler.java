package com.progress.tracking.rest.util;

import com.progress.tracking.util.exception.CourseNotFoundException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.PlatformNotFoundException;
import com.progress.tracking.util.exception.WrapperExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WrapperExecutionException.class)
    public ProblemDetail handleWrapperExecutionException(final WrapperExecutionException ex) {
        log.error("Error while executing wrapper", ex);
        return createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ProblemDetail handleInvalidParameterException(final InvalidParameterException ex) {
        log.error("Invalid parameter", ex);
        return createProblemDetail(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ProblemDetail handleCourseNotFoundException(final CourseNotFoundException ex) {
        log.error("Course not found", ex);
        return createProblemDetail(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlatformNotFoundException.class)
    public ProblemDetail handlePlatformNotFoundException(final PlatformNotFoundException ex) {
        log.error("Platform not found", ex);
        return createProblemDetail(ex, HttpStatus.NOT_FOUND);
    }

    private ProblemDetail createProblemDetail(final Throwable ex, final HttpStatus status) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(ex.getMessage());
        problemDetail.setDetail(ex.getCause() != null ? ex.getCause().getMessage() : null);
        return problemDetail;
    }

}
