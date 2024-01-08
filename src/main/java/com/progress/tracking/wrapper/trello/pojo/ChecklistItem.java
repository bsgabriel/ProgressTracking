package com.progress.tracking.wrapper.trello.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChecklistItem {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("state")
    @Expose
    private boolean state;

    @SerializedName("idChecklist")
    @Expose
    private String idChecklist;

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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getIdChecklist() {
        return idChecklist;
    }

    public void setIdChecklist(String idChecklist) {
        this.idChecklist = idChecklist;
    }
}
