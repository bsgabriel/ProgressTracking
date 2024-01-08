package com.progress.tracking.entity;

import com.progress.tracking.wrapper.udemy.pojo.Result;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UdemyCourseCurriculum {
    private String title;
    private String headline;
    private Map<Result, List<Result>> chapters;

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

    public Map<Result, List<Result>> getChapters() {
        if (chapters == null)
            chapters = new LinkedHashMap<>(0);

        return chapters;
    }

    public void setChapters(Map<Result, List<Result>> chapters) {
        this.chapters = chapters;
    }
}
