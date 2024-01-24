package com.progress.tracking.wrapper.trello;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progress.tracking.util.WsUtil;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.wrapper.trello.pojo.*;

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
     * Searches for Trello boards based on the provided board name.
     *
     * @param boardName Name of the Trello board to search for.
     * @return List of {@linkplain Board} objects matching the search criteria.
     * @throws InvalidParameterException If the boardName is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public List<Board> searchBoardByName(final String boardName) throws InvalidParameterException, ApiExecutionException {
        if (boardName == null || boardName.isBlank())
            throw new InvalidParameterException("boardName");

        final String url = BASE_URL + "search";

        final Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("token", apiToken);
        params.put("query", boardName);

        try {
            return doRequest(url, params, TrelloSearchResponse.class).getBoards();
        } catch (Exception e) {
            throw new ApiExecutionException("An error occurred while searching for the board '" + boardName + "'.", e);
        }
    }

    /**
     * Retrieves lists from a Trello board based on the provided board ID.
     *
     * @param boardId Trello board ID.
     * @return List of {@linkplain TrelloList} objects associated with the board.
     * @throws InvalidParameterException If the boardId is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public List<TrelloList> getListsFromBoard(final String boardId) throws InvalidParameterException, ApiExecutionException {
        if (boardId == null || boardId.isBlank())
            throw new InvalidParameterException("boardId");

        final String url = BASE_URL + "boards/" + boardId + "/lists";

        final Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("token", apiToken);

        final String jsonResponse;
        try {
            jsonResponse = WsUtil.sendGet(url, createHeader(), params);
        } catch (Exception e) {
            throw new ApiExecutionException("An error occurred while retrieving lists from the board: " + boardId, e);
        }

        return gson.fromJson(jsonResponse, new TypeToken<ArrayList<TrelloList>>() {
        }.getType());
    }

    /**
     * Creates a Trello board with the provided name and description.
     *
     * @param boardName Name of the Trello board.
     * @param desc      Description of the Trello board.
     * @return {@linkplain Board} object representing the created board.
     * @throws InvalidParameterException If the boardName is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public Board createBoard(final String boardName, final String desc) throws InvalidParameterException, ApiExecutionException {
        if (boardName == null || boardName.isBlank())
            throw new InvalidParameterException("boardName");

        final String url = BASE_URL + "boards/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(boardName);
        req.setDescription(desc);
        req.setDefaultLists(false);
        req.setDefaultLabels(false);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        try {
            return doRequest(url, req, Board.class);
        } catch (Exception e) {
            throw new ApiExecutionException("An error occurred while creating a board. {name='" + boardName + "', description='" + desc + "'}.", e);
        }
    }

    /**
     * Creates a Trello list on the specified board with the provided name.
     *
     * @param idBoard Trello board ID.
     * @param name    Name of the Trello list.
     * @return {@linkplain TrelloList} object representing the created list.
     * @throws InvalidParameterException If the either the idBoard or name is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public TrelloList createList(final String idBoard, final String name) throws InvalidParameterException, ApiExecutionException {
        if (idBoard == null || idBoard.isBlank())
            throw new InvalidParameterException("idBoard");

        if (name == null || name.isBlank())
            throw new InvalidParameterException("name");

        final String url = BASE_URL + "list/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setIdBoard(idBoard);
        req.setApiToken(apiToken);
        req.setApiKey(apiKey);

        try {
            return doRequest(url, req, TrelloList.class);
        } catch (Exception e) {
            throw new ApiExecutionException("An error occurred while creating a list into the board: " + idBoard, e);
        }
    }

    /**
     * Creates a Trello card on the specified list with the provided name and description.
     *
     * @param idList ID of the Trello list.
     * @param name   Name of the Trello card.
     * @param desc   Description of the Trello card.
     * @return {@linkplain Card} object representing the created card.
     * @throws InvalidParameterException If the either the idList or name is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public Card createCard(final String idList, final String name, final String desc) throws InvalidParameterException, ApiExecutionException {
        if (idList == null || idList.isBlank())
            throw new InvalidParameterException("idList");

        if (name == null || name.isBlank())
            throw new InvalidParameterException("name");

        final String url = BASE_URL + "cards/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setDescription(desc);
        req.setIdList(idList);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        try {
            return doRequest(url, req, Card.class);
        } catch (Exception e) {
            throw new ApiExecutionException("An error occured while creating a card.", e);
        }
    }

    /**
     * Creates a Trello checklist on the specified card with the provided name.
     *
     * @param idCard ID of the Trello card.
     * @param name   Name of the Trello checklist.
     * @return {@linkplain Checklist} object representing the created checklist.
     * @throws InvalidParameterException If the either the idCard or name is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public Checklist createChecklist(final String idCard, final String name) throws InvalidParameterException, ApiExecutionException {
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
            throw new ApiExecutionException("An error ocurred while creating a checklist for the card: " + idCard, e);
        }
    }

    /**
     * Creates a checklist item in the specified checklist with the provided name.
     *
     * @param idChecklist ID of the Trello checklist.
     * @param name        Name of the checklist item.
     * @return {@linkplain ChecklistItem} object representing the created checklist item.
     * @throws InvalidParameterException If the either the idChecklist or name is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public ChecklistItem createChecklistItem(final String idChecklist, final String name) throws InvalidParameterException, ApiExecutionException {
        if (idChecklist == null || idChecklist.isBlank())
            throw new InvalidParameterException("idChecklist");

        if (name == null || name.isBlank())
            throw new InvalidParameterException("name");

        final String url = BASE_URL + "checklists/" + idChecklist + "/checkItems/";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        try {
            return doRequest(url, req, ChecklistItem.class);
        } catch (Exception e) {
            throw new ApiExecutionException("An error ocurred while creating a checklist item for the checklist: " + idChecklist, e);
        }
    }

    /**
     * Attaches a link to the specified Trello card.
     *
     * @param idCard ID of the Trello card.
     * @param name   Name to be displayed for the attached link. If null, the link itself will be displayed.
     * @param attUrl URL of the link to be attached.
     * @return {@linkplain CardAttachment} object representing the created card attachment.
     * @throws InvalidParameterException If idCard or attUrl is null or blank.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public CardAttachment createCardUrlAttachment(final String idCard, final String name, final String attUrl) throws InvalidParameterException, ApiExecutionException {
        if (idCard == null || idCard.isBlank())
            throw new InvalidParameterException("idCard");

        if (attUrl == null || attUrl.isBlank())
            throw new InvalidParameterException("attUrl");

        final String url = BASE_URL + "cards/" + idCard + "/attachments";
        final TrelloRequest req = new TrelloRequest();
        req.setName(name);
        req.setUrl(attUrl);
        req.setApiKey(apiKey);
        req.setApiToken(apiToken);

        try {
            return doRequest(url, req, CardAttachment.class);
        } catch (Exception e) {
            throw new ApiExecutionException("An error ocurred while creating an attachment for the card: " + idCard, e);
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
