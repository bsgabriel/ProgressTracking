package com.progress.tracking.rest.dto.udemy;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UdemyVisibleInstructorDto {

    private String title;
    private String name;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("job_title")
    private String jobTitle;

    private String initials;
    private String url;

}