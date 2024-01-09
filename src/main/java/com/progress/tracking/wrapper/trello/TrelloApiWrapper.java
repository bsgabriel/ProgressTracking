package com.progress.tracking.wrapper.trello;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progress.tracking.util.WsUtil;
import com.progress.tracking.wrapper.trello.pojo.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrelloApiWrapper {
    private static final String BASE_URL = "https://api.trello.com/1/";
    private static final Gson gson = new Gson();
    private String apiKey;
    private String apiToken;

    private TrelloApiWrapper() {
    }

    /**
     * Initializes the wrapper
     *
     * @param apiKey
     * @param apiToken
     * @return instance of the wrapper
     */
    public static TrelloApiWrapper initialize(String apiKey, String apiToken) {
        // TODO throw exception if there's no key or token
        TrelloApiWrapper wrapper = new TrelloApiWrapper();
        wrapper.apiKey = apiKey;
        wrapper.apiToken = apiToken;
        return wrapper;
    }

    private Map<String, String> createHeader() {
        Map header = new HashMap();
        header.put("Content-Type", "application/json");
        return header;
    }

    public List<Board> searchBoardByName(String boardName) {
        final String url = BASE_URL + "search";

        final Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("token", apiToken);
        params.put("query", boardName);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendGet(url, createHeader(), params);
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, TrelloSearchResponse.class).getBoards();
    }

    public List<TrelloList> getListsFromBoard(final String boardId) {
        final String url = BASE_URL + "boards/" + boardId + "/lists";

        final Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("token", apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendGet(url, createHeader(), params);
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        final Type listType = new TypeToken<ArrayList<TrelloList>>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }


    public Board createBoard(final String boardName, final String desc) {
        // TODO throw exception if there's no boardName
        final String url = BASE_URL + "boards/";
        TrelloRequest req = new TrelloRequest();
        req.setName(boardName);
        req.setDescription(desc);
        req.setDefaultLists(false);
        req.setDefaultLabels(false);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, Board.class);
    }

    public TrelloList createList(final String idBoard, final String name) {
        // TODO throw exception if there's no name or idBoard
        final String url = BASE_URL + "list/";
        TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setIdBoard(idBoard);
        req.setApiToken(apiToken);
        req.setApiKey(apiKey);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, TrelloList.class);
    }

    public Card createCard(final String idList, final String name, final String desc) {
        // TODO throw exception if there's no name or idList
        final String url = BASE_URL + "cards/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setDescription(desc);
        req.setIdList(idList);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO Log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, Card.class);
    }

    public Checklist createChecklist(final String idCard, final String name) {
        // TODO throw exception if there's no name or idCard

        final String url = BASE_URL + "checklists";
        final TrelloRequest req = new TrelloRequest();
        req.setIdCard(idCard);
        req.setName(name);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, Checklist.class);
    }

    public ChecklistItem createChecklistItem(final String idChecklist, final String name) {
        // TODO throw exception if there's no name or idCard
        final String url = BASE_URL + "checklists/" + idChecklist + "/checkItems/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(url, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, ChecklistItem.class);
    }

    public CardAttachment createCardAttachment(final String idCard, final String name, final String url) {
        // TODO throw exception if there's no idCard
        final String urlReq = BASE_URL + "cards/" + idCard + "/attachments";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setUrl(url);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendPost(urlReq, createHeader(), gson.toJson(req));
        } catch (Exception e) {
            // TODO log
            throw new RuntimeException(e);
        }

        return gson.fromJson(jsonResponse, CardAttachment.class);
    }
}
