package com.progress.tracking.response.udemy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisibleInstructor {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @SerializedName("initials")
    @Expose
    private String initials;

    @SerializedName("url")
    @Expose
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