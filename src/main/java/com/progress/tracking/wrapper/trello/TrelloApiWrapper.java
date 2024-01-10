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
     * @param apiKey   Trello API key
     * @param apiToken Trello API token
     * @return Instance of the TrelloApiWrapper
     */
    public static TrelloApiWrapper initialize(String apiKey, String apiToken) {
        // TODO throw exception if there's no key or token
        TrelloApiWrapper wrapper = new TrelloApiWrapper();
        wrapper.apiKey = apiKey;
        wrapper.apiToken = apiToken;
        return wrapper;
    }

    /**
     * Creates the header with the necessary content type for Trello API requests.
     *
     * @return Map representing the HTTP headers
     */
    private Map<String, String> createHeader() {
        final Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * Searches for Trello boards based on the provided board name.
     *
     * @param boardName Name of the Trello board to search for
     * @return List of Board objects matching the search criteria
     */
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

    /**
     * Retrieves lists from a Trello board based on the provided board ID.
     *
     * @param boardId Trello board ID
     * @return List of TrelloList objects associated with the board
     */
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

    /**
     * Creates a Trello board with the provided name and description.
     *
     * @param boardName Name of the Trello board
     * @param desc      Description of the Trello board
     * @return Board object representing the created board
     */
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

    /**
     * Creates a Trello list on the specified board with the provided name.
     *
     * @param idBoard Trello board ID
     * @param name    Name of the Trello list
     * @return TrelloList object representing the created list
     */
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

    /**
     * Creates a Trello card on the specified list with the provided name and description.
     *
     * @param idList ID of the Trello list
     * @param name   Name of the Trello card
     * @param desc   Description of the Trello card
     * @return Card object representing the created card
     */
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

    /**
     * Creates a Trello checklist on the specified card with the provided name.
     *
     * @param idCard ID of the Trello card
     * @param name   Name of the Trello checklist
     * @return Checklist object representing the created checklist
     */
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

    /**
     * Creates a checklist item in the specified checklist with the provided name.
     *
     * @param idChecklist ID of the Trello checklist
     * @param name        Name of the checklist item
     * @return ChecklistItem object representing the created checklist item
     */
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

    /**
     * Attaches a file to the specified Trello card.
     *
     * @param idCard ID of the Trello card
     * @param name   Name of the file attachment
     * @param url    URL of the file to be attached
     * @return CardAttachment object representing the created card attachment
     */
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
