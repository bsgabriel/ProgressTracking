package com.progress.tracking.rest.request;

public class SearchCourseRequest extends AbstractRequest {
    private String course;
    private Integer max;
    private Integer page;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
