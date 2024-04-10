package com.progress.tracking.rest.response;

public class CourseToTrelloResponse extends AbstractResponse {
    private String boardUrl;

    public String getBoardUrl() {
        return boardUrl;
    }

    public void setBoardUrl(String boardUrl) {
        this.boardUrl = boardUrl;
    }
}
