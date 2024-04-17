package com.progress.tracking.rest.entity;

import java.util.ArrayList;
import java.util.List;

public class Chapter {

    private String name;
    private List<String> lessons;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLessons() {
        if (this.lessons == null)
            this.lessons = new ArrayList<>();
        return lessons;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }
}
