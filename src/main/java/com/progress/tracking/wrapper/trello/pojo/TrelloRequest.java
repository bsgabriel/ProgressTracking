package com.progress.tracking.wrapper.trello.pojo;

import com.google.gson.annotations.SerializedName;

public class TrelloRequest {

    private String name;
    private String idOrganization;
    private String idBoard;
    private String idList;
    private String idCard;
    private boolean defaultLists;
    private boolean defaultLabels;
    private String url;

    @SerializedName("token")
    private String apiToken;

    @SerializedName("key")
    private String apiKey;

    @SerializedName("desc")
    private String description;

    @SerializedName("pos")
    private Integer position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(String idOrganization) {
        this.idOrganization = idOrganization;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
