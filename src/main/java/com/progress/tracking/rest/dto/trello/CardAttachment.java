package com.progress.tracking.rest.dto.trello;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAttachment {

    private String id;
    private String name;
    private String url;

}
