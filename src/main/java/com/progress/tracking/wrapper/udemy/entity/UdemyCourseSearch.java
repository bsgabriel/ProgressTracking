package com.progress.tracking.wrapper.udemy.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UdemyCourseSearch {
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private List<UdemyCourse> courses;
    private Integer count;
}
