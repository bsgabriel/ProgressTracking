package com.progress.tracking.rest.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCourseRequest {
    private String udemyClientId;
    private String udemyClientSecret;
    private String course;
    private Integer max;
    private Integer page;
}
