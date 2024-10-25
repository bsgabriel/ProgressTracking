package com.progress.tracking.rest.dto.trello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private String id;
    private String name;
    private String idBoard;
    private String idList;
    private String url;
    private String shortUrl;

    @JsonProperty("desc")
    private String description;
}
