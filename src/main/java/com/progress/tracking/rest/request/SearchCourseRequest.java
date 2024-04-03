package com.progress.tracking.rest.request;

public class SearchCourseRequest {
    private String udemyClientId;
    private String udemyClientSecret;
    private String course;
    private Integer max;
    private Integer page;

    public String getUdemyClientId() {
        return udemyClientId;
    }

    public void setUdemyClientId(String udemyClientId) {
        this.udemyClientId = udemyClientId;
    }

    public String getUdemyClientSecret() {
        return udemyClientSecret;
    }

    public void setUdemyClientSecret(String udemyClientSecret) {
        this.udemyClientSecret = udemyClientSecret;
    }

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
