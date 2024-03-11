package com.progress.tracking.rest.request;

public abstract class AbstractRequest {
    private String udemyClientId;
    private String udemyClientSecret;
    private String trelloApiKey;
    private String trelloApiToken;

    public String getUdemyClientId() {
        return udemyClientId;
    }

    public void setUdemyClientId(String udemyClientId) {
        this.udemyClientId = udemyClientId;
    }

    public String getUdemyClientSecret() {
        return udemyClientSecret;
    }

    public void setUdemyClientSecret(String udemyClientSecret) {
        this.udemyClientSecret = udemyClientSecret;
    }

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
}
