package com.progress.tracking.response.trello;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Checklist {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("idBoard")
    @Expose
    private String idBoard;

    @SerializedName("idCard")
    @Expose
    private String idCard;

    @SerializedName("checkItems")
    @Expose
    private List<Object> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public List<Object> getItems() {
        if (items == null)
            items = new ArrayList<>();

        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

}
