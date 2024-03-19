package com.progress.tracking.wrapper.udemy.entity;

import java.util.ArrayList;
import java.util.List;

public class UdemyCourseSearch {
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private List<UdemyCourse> courses;

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

    public Integer getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(Integer previousPage) {
        this.previousPage = previousPage;
    }

    public List<UdemyCourse> getCourses() {
        if (this.courses == null)
            courses = new ArrayList<>();
        return courses;
    }

    public void setCourses(List<UdemyCourse> courses) {
        this.courses = courses;
    }
}
