package com.progress.tracking.entity;

import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;

import java.util.ArrayList;
import java.util.List;

public class UdemyCourse {
    private Integer id;
    private String title;
    private String headline;
    private List<VisibleInstructor> instructors;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public List<VisibleInstructor> getInstructors() {
        if (instructors == null)
            instructors = new ArrayList<>();
        return instructors;
    }

    public void setInstructors(List<VisibleInstructor> instructors) {
        this.instructors = instructors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UdemySearchedCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headline='" + headline + '\'' +
                ", instructors=" + instructors +
                ", url='" + url + '\'' +
                '}';
    }
}
