package com.progress.tracking.response.trello;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCardRequest {
    @SerializedName("idList")
    @Expose
    private String idList;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("desc")
    @Expose
    private String description;

    @SerializedName("key")
    @Expose
    private String apiKey;

    @SerializedName("token")
    @Expose
    private String apiToken;

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
