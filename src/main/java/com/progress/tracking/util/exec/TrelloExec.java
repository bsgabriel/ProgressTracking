package com.progress.tracking.util.exec;

import com.progress.tracking.rest.request.AbstractRequest;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperInitializationException;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
import com.progress.tracking.wrapper.trello.pojo.*;
import com.progress.tracking.wrapper.udemy.pojo.Result;

import java.util.List;
import java.util.Map;

public class TrelloExec {

    final private TrelloApiWrapper tWrapper;

    public TrelloExec(final AbstractRequest req) throws WrapperInitializationException {
        try {
            this.tWrapper = TrelloApiWrapper.initialize(req.getTrelloApiKey(), req.getTrelloApiToken());
        } catch (InvalidParameterException e) {
            throw new WrapperInitializationException("Couldn't initialize Trello's API wrapper", e);
        }
    }

    public Board searchBoardByName(final String boardName, final String description) throws ApiExecutionException, InvalidParameterException {
        final List<Board> boards = this.tWrapper.searchBoardByName(boardName);
        final Board board;
        if (boards.isEmpty())
            board = this.createTrelloBoard(boardName, description);
        else
            board = boards.get(0);

        return board;
    }

    public Board createTrelloBoard(String boardName, String desc) throws ApiExecutionException, InvalidParameterException {
        final Board board = this.tWrapper.createBoard(boardName, desc);

        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("name: ").append(board.getName()).append("\n");
        sb.append("\t").append("description: ").append(board.getDesc()).append("\n");
        sb.append("\t").append("board's ID: ").append(board.getId()).append("\n");
        sb.append("\t").append("organization's ID: ").append(board.getIdOrganization()).append("\n");
        sb.append("\t").append("url: ").append(board.getUrl()).append("\n");
        sb.append("\t").append("short url: ").append(board.getShortUrl()).append("\n");
        System.out.println(sb);

        return board;
    }

    public TrelloList createTrelloList(final String idBoard, final String name) throws ApiExecutionException, InvalidParameterException {
        TrelloList list = this.tWrapper.createList(idBoard, name);

        StringBuilder sb = new StringBuilder();
        sb.append("Created list:").append("\n");
        sb.append("\t").append("name: ").append(list.getName()).append("\n");
        sb.append("\t").append("id: ").append(list.getId()).append("\n");
        sb.append("\t").append("board's ID: ").append(list.getIdBoard()).append("\n");
        System.out.println(sb);

        return list;
    }

    public Card createTrelloCard(String idList, String name, String desc) throws ApiExecutionException, InvalidParameterException {
        Card card = this.tWrapper.createCard(idList, name, desc);

        StringBuilder sb = new StringBuilder();
        sb.append("Created card:").append("\n");
        sb.append("\t").append("id: ").append(card.getId()).append("\n");
        sb.append("\t").append("name: ").append(card.getName()).append("\n");
        sb.append("\t").append("description: ").append(card.getDescription()).append("\n");
        sb.append("\t").append("list: ").append(card.getIdList()).append("\n");
        sb.append("\t").append("board: ").append(card.getIdBoard()).append("\n");
        sb.append("\t").append("url: ").append(card.getUrl()).append("\n");
        sb.append("\t").append("short url: ").append(card.getShortUrl()).append("\n");
        System.out.println(sb);

        return card;
    }

    public void attachCourseLink(String idCard, String attName, String attUrl) throws ApiExecutionException, InvalidParameterException {
        CardAttachment att = this.tWrapper.createCardUrlAttachment(idCard, attName, attUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("Anexo criado:").append("\n");
        sb.append("\t").append("id: ").append(att.getId()).append("\n");
        sb.append("\t").append("name: ").append(att.getName()).append("\n");
        sb.append("\t").append("url: ").append(att.getUrl()).append("\n");
        System.out.println(sb);
    }

    private Checklist createChecklistForCard(String idCard, String name) throws ApiExecutionException, InvalidParameterException {
        Checklist lst = this.tWrapper.createChecklist(idCard, name);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist:").append("\n");
        sb.append("\t").append("id: ").append(lst.getId()).append("\n");
        sb.append("\t").append("name: ").append(lst.getName()).append("\n");
        sb.append("\t").append("card's ID: ").append(lst.getIdCard()).append("\n");
        sb.append("\t").append("board's ID: ").append(lst.getIdBoard()).append("\n");
        sb.append("\t").append("items: ").append(lst.getItems()).append("\n");
        System.out.println(sb);

        return lst;
    }

    private void createItemForChecklist(String idChecklist, String name) throws ApiExecutionException, InvalidParameterException {
        ChecklistItem item = this.tWrapper.createChecklistItem(idChecklist, name);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist item:").append("\n");
        sb.append("\t").append("id: ").append(item.getId()).append("\n");
        sb.append("\t").append("name: ").append(item.getName()).append("\n");
        sb.append("\t").append("checklist's ID: ").append(item.getIdChecklist()).append("\n");
        System.out.println(sb);
    }

    public TrelloList searchListFromBoard(final String idBoard, final String list) throws ApiExecutionException, InvalidParameterException {
        List<TrelloList> lists = this.tWrapper.getListsFromBoard(idBoard);

        if (lists == null || lists.isEmpty())
            return createTrelloList(idBoard, list);

        TrelloList tList = null;
        for (TrelloList l : lists) {
            if (l.getName().equalsIgnoreCase(list)) {
                tList = l;
                break;
            }
        }

        if (tList == null)
            return createTrelloList(idBoard, list);

        return tList;
    }

    public void createChecklists(final Map<Result, List<Result>> chapters, final Card card) throws ApiExecutionException, InvalidParameterException {
        int idxItem = 1;
        for (Result chapter : chapters.keySet()) {
            final List<Result> lectures = chapters.get(chapter);
            if (lectures == null || lectures.isEmpty())
                continue;

            final Checklist checkList = this.createChecklistForCard(card.getId(), chapter.getTitle());

            for (Result lecture : lectures) {
                this.createItemForChecklist(checkList.getId(), idxItem + ". " + lecture.getTitle());
                idxItem++;
            }
        }
    }
}
