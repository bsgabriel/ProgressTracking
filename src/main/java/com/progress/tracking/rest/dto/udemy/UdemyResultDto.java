package com.progress.tracking.rest.dto.udemy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UdemyResultDto {

    @JsonProperty("_class")
    private String type;
    private Integer id;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    private String title;
    private String description;

    @JsonProperty("visible_instructors")
    private List<UdemyVisibleInstructorDto> visibleInstructors;

    private String url;
    private String headline;

    @JsonProperty("image_480x270")
    private String image;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UdemyResultDto result))
            return false;

        if (!type.equals(result.type))
            return false;
        if (!getId().equals(result.getId()))
            return false;
        if (!getSortOrder().equals(result.getSortOrder()))
            return false;
        if (!getTitle().equals(result.getTitle()))
            return false;
        if (!getImage().equals(result.getImage()))
            return false;
        return getDescription().equals(result.getDescription());
    }

}
