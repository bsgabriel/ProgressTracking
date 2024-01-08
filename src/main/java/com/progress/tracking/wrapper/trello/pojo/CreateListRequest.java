package com.progress.tracking.wrapper.trello.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateListRequest {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("idBoard")
    @Expose
    private String idBoard;

    @SerializedName("key")
    @Expose
    private String apiKey;

    @SerializedName("token")
    @Expose
    private String apiToken;

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
