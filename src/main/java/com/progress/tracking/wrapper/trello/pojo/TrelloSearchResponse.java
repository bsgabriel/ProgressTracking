package com.progress.tracking.wrapper.trello.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrelloSearchResponse {

    @SerializedName("boards")
    @Expose
    private List<Board> boards;

    public List<Board> getBoards() {
        if (boards == null)
            boards = new ArrayList<>();
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }
}