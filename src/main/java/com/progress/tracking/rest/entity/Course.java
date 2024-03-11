package com.progress.tracking.rest.entity;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String name;
    private String desc;
    private List<String> instructors;
    private String image;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getInstructors() {
        if (this.instructors == null)
            this.instructors = new ArrayList<>();
        return instructors;
    }

    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
