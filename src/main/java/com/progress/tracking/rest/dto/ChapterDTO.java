package com.progress.tracking.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChapterDTO {
    private String name;
    private List<String> lessons;
}
