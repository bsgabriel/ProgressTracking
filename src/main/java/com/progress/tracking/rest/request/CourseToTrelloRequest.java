package com.progress.tracking.rest.request;

import com.progress.tracking.rest.dto.CourseDTO;

public class CourseToTrelloRequest {
    private String trelloApiKey;
    private String trelloApiToken;
    private String boardName;
    private String boardDescription;
    private String listName;
    private CourseDTO course;

    public String getTrelloApiKey() {
        return trelloApiKey;
    }

    public void setTrelloApiKey(String trelloApiKey) {
        this.trelloApiKey = trelloApiKey;
    }

    public String getTrelloApiToken() {
        return trelloApiToken;
    }

    public void setTrelloApiToken(String trelloApiToken) {
        this.trelloApiToken = trelloApiToken;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardDescription() {
        return boardDescription;
    }

    public void setBoardDescription(String boardDescription) {
        this.boardDescription = boardDescription;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }
}
