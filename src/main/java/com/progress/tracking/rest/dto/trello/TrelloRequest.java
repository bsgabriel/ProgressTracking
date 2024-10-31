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
public class TrelloRequest {

    private String name;
    private String idOrganization;
    private String idBoard;
    private String idList;
    private String idCard;
    private boolean defaultLists;
    private boolean defaultLabels;
    private String url;

    @JsonProperty("token")
    private String apiToken;

    @JsonProperty("key")
    private String apiKey;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("pos")
    private Integer position;

}
