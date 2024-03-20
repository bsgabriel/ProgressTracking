package com.progress.tracking.rest.entity;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private Integer id;
    private String name;
    private String desc;
    private String image;
    private String url;
    private List<Chapter> chapters;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<Chapter> getChapters() {
        if (this.chapters == null)
            this.chapters = new ArrayList<>();
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
