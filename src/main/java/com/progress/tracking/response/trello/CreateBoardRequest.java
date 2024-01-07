package com.progress.tracking.response.trello;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateBoardRequest {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("idOrganization")
    @Expose
    private String idOrganization;

    @SerializedName("defaultLists")
    @Expose
    private boolean defaultLists;

    @SerializedName("defaultLabels")
    @Expose
    private boolean defaultLabels;

    @SerializedName("token")
    @Expose
    private String apiToken;

    @SerializedName("key")
    @Expose
    private String apiKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(String idOrganization) {
        this.idOrganization = idOrganization;
    }

    public boolean isDefaultLists() {
        return defaultLists;
    }

    public void setDefaultLists(boolean defaultLists) {
        this.defaultLists = defaultLists;
    }

    public boolean isDefaultLabels() {
        return defaultLabels;
    }

    public void setDefaultLabels(boolean defaultLabels) {
        this.defaultLabels = defaultLabels;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
