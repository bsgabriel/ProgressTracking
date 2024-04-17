package com.progress.tracking.rest.response;

import com.progress.tracking.rest.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class SearchCourseResponse {
    private Integer currentPage;
    private Integer nextPage;
    private Integer previsouPage;
    private List<Course> courses;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPrevisouPage() {
        return previsouPage;
    }

    public void setPrevisouPage(Integer previsouPage) {
        this.previsouPage = previsouPage;
    }

    public List<Course> getCourses() {
        if (this.courses == null)
            courses = new ArrayList<>();
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
