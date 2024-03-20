package com.progress.tracking.rest.request;

import com.progress.tracking.rest.entity.Course;

public class CourseToTrelloRequest extends AbstractRequest {
    private String boardName;
    private String boardDescription;
    private String listName;
    private Course course;

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
