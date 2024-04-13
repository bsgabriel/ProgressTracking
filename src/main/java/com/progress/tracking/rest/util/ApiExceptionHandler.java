package com.progress.tracking.rest.util;

import com.progress.tracking.util.exception.CourseNotFoundException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WrapperExecutionException.class)

    public ProblemDetail handleWrapperExecutionException(final WrapperExecutionException ex) {
        return createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ProblemDetail handleInvalidParameterException(final InvalidParameterException ex) {
        return createProblemDetail(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ProblemDetail handleCourseNotFoundException(final CourseNotFoundException ex) {
        final ProblemDetail problemDetail = createProblemDetail(ex, HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getDetail());
        return problemDetail;
    }

    private ProblemDetail createProblemDetail(final Throwable ex, final HttpStatus status) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(ex.getMessage());
        problemDetail.setDetail(ex.getCause() != null ? ex.getCause().getMessage() : null);
        return problemDetail;
    }

}
