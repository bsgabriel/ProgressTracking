package com.progress.tracking.rest.dto.udemy;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UdemyResponseDto {

    private Integer count;
    private String next;
    private String previous;
    private List<UdemyResultDto> results;

}
