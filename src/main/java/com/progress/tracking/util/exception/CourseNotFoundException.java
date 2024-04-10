package com.progress.tracking.util.exception;

public class CourseNotFoundException extends RuntimeException {
    private final String courseName;
    private final String source;

    public CourseNotFoundException(final String courseName, final String source) {
        super("Couldn't find the specified course.");
        this.courseName = courseName;
        this.source = source;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSource() {
        return source;
    }
}
