package com.progress.tracking.wrapper.udemy.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private String _class;
    private Integer id;

    @SerializedName("sort_order")
    private Integer sortOrder;

    private String title;
    private String description;

    @SerializedName("visible_instructors")
    private List<VisibleInstructor> visibleInstructors;

    private String url;
    private String headline;

    @SerializedName("image_480x270")
    private String image;

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VisibleInstructor> getVisibleInstructors() {
        if (visibleInstructors == null)
            visibleInstructors = new ArrayList<>();

        return visibleInstructors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Result result))
            return false;

        if (!_class.equals(result._class))
            return false;
        if (!getId().equals(result.getId()))
            return false;
        if (!getSortOrder().equals(result.getSortOrder()))
            return false;
        if (!getTitle().equals(result.getTitle()))
            return false;
        if (!getImage().equals(result.getImage()))
            return false;
        return getDescription().equals(result.getDescription());
    }

}
