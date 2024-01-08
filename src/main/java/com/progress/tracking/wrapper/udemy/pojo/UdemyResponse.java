package com.progress.tracking.wrapper.udemy.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UdemyResponse {

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("results")
    @Expose
    private List<Result> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Result> getResults() {
        if (results == null)
            results = new ArrayList<>();
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
