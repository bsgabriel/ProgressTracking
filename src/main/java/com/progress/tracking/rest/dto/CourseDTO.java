package com.progress.tracking.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseDTO {
    private Integer id;
    private String name;
    private String desc;
    private String image;
    private String url;
    private List<ChapterDTO> chapters;
}
