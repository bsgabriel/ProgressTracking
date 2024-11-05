package com.progress.tracking.rest.dto.udemy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UdemyVisibleInstructorDto {

    private String title;
    private String name;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("job_title")
    private String jobTitle;

    private String initials;
    private String url;

}