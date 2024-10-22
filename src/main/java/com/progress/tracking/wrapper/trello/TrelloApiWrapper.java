package com.progress.tracking.wrapper.trello;

import com.google.gson.Gson;
import com.progress.tracking.util.WsUtil;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperExecutionException;
import com.progress.tracking.wrapper.trello.pojo.Checklist;
import com.progress.tracking.wrapper.trello.pojo.ChecklistItem;
import com.progress.tracking.wrapper.trello.pojo.TrelloRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * TrelloApiWrapper is a utility class for interacting with the Trello API to perform various operations on boards, lists, cards, checklists, and attachments.
 * For more details on the Trello API, refer to the <a href="https://developer.atlassian.com/cloud/trello/rest/">official documentation</a>.
 */
public class TrelloApiWrapper {
    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final Gson gson = new Gson();
    private String apiKey;
    private String apiToken;

    private TrelloApiWrapper() {
    }

    /**
     * Initializes the wrapper with the provided API key and token.
     *
     * @param apiKey   Trello API key.
     * @param apiToken Trello API token.
     * @return Instance of the TrelloApiWrapper.
     * @throws InvalidParameterException If either the apiKey or apiToken is null or blank.
     */
    public static TrelloApiWrapper initialize(final String apiKey, final String apiToken) throws InvalidParameterException {
        if (apiKey == null || apiKey.isBlank())
            throw new InvalidParameterException("apiKey");

        if (apiToken == null || apiToken.isBlank())
            throw new InvalidParameterException("apiToken");

        final TrelloApiWrapper wrapper = new TrelloApiWrapper();
        wrapper.apiKey = apiKey;
        wrapper.apiToken = apiToken;
        return wrapper;
    }

    /**
     * Creates the header with the necessary content type for Trello API requests.
     *
     * @return Map representing the HTTP headers.
     */
    private Map<String, String> createHeader() {
        final Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * Creates a Trello checklist on the specified card with the provided name.
     *
     * @param idCard ID of the Trello card.
     * @param name   Name of the Trello checklist.
     * @return {@linkplain Checklist} object representing the created checklist.
     * @throws InvalidParameterException If the either the idCard or name is null or blank.
     * @throws WrapperExecutionException     If an error occurs during the API call.
     */
    public Checklist createChecklist(final String idCard, final String name) throws InvalidParameterException, WrapperExecutionException {
        if (idCard == null || idCard.isBlank())
            throw new InvalidParameterException("idCard");

        if (name == null || name.isBlank())
            throw new InvalidParameterException("name");

        final String url = BASE_URL + "checklists";
        final TrelloRequest req = new TrelloRequest();
        req.setIdCard(idCard);
        req.setName(name);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        try {
            return doRequest(url, req, Checklist.class);
        } catch (Exception e) {
            throw new WrapperExecutionException("An error ocurred while creating a checklist for the card: " + idCard, e);
        }
    }

    /**
     * Creates a checklist item in the specified checklist with the provided name.
     *
     * @param idChecklist ID of the Trello checklist.
     * @param name        Name of the checklist item.
     * @return {@linkplain ChecklistItem} object representing the created checklist item.
     * @throws InvalidParameterException If the either the idChecklist or name is null or blank.
     * @throws WrapperExecutionException     If an error occurs during the API call.
     */
    public ChecklistItem createChecklistItem(final String idChecklist, final String name, final Integer idx) throws InvalidParameterException, WrapperExecutionException {
        if (idChecklist == null || idChecklist.isBlank())
            throw new InvalidParameterException("idChecklist");

        if (name == null || name.isBlank())
            throw new InvalidParameterException("name");

        final String url = BASE_URL + "checklists/" + idChecklist + "/checkItems/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);
        req.setPosition(idx);

        try {
            return doRequest(url, req, ChecklistItem.class);
        } catch (Exception e) {
            throw new WrapperExecutionException("An error ocurred while creating a checklist item for the checklist: " + idChecklist, e);
        }
    }

    private <T> T doRequest(final String url, final Object request, final Class<T> returnClass) throws Exception {
        return doRequest(url, request, null, returnClass);
    }

    private <T> T doRequest(final String url, final Map<String, String> params, final Class<T> returnClass) throws Exception {
        return doRequest(url, null, params, returnClass);
    }

    private <T> T doRequest(final String url, final Object postRequest, final Map<String, String> getParams, final Class<T> returnClass) throws Exception {
        final String jsonResponse;
        if (postRequest != null)
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(postRequest));
        else
            jsonResponse = WsUtil.sendGet(url, createHeader(), getParams);

        return gson.fromJson(jsonResponse, returnClass);
    }
}
