package com.progress.tracking.rest.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Platform {
    private Info info;
    private List<Field> fields;

    @Getter
    @Builder
    public static class Info {
        private String name;
        private String logo;
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Field {
        private String name;
        private String description;
        private boolean required;
        private String defaultValue;
    }
}
