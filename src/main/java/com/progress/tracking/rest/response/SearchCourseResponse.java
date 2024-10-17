package com.progress.tracking.rest.response;

import com.progress.tracking.rest.dto.CourseDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchCourseResponse {
    private Integer currentPage;
    private Integer nextPage;
    private Integer previsouPage;
    private List<CourseDTO> courses;
    private Integer count;
}
