package com.progress.tracking.wrapper.udemy.pojo;

import java.util.ArrayList;
import java.util.List;

public class UdemyResponse {

    private Integer count;
    private String next;
    private String previous;
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
