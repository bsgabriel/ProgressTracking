package com.progress.tracking.rest.dto.trello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {

    private String id;
    private String name;
    private String idBoard;
    private String idCard;

    @JsonProperty("checkItems")
    private List<Object> checkItems;

}
