package com.progress.tracking.wrapper.udemy.pojo;

import com.google.gson.annotations.SerializedName;

public class VisibleInstructor {

    private String title;
    private String name;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("job_title")
    private String jobTitle;

    private String initials;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VisibleInstructor{" + "title='" + title + '\'' + ", name='" + name + '\'' + ", displayName='" + displayName + '\'' + ", jobTitle='" + jobTitle + '\'' + ", initials='" + initials + '\'' + ", url='" + url + '\'' + '}';
    }
}