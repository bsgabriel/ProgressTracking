package com.progress.tracking.rest.dto.udemy;

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
