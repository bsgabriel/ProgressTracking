package com.progress.tracking.rest.service;

import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.wrapper.trello.pojo.Board;
import com.progress.tracking.wrapper.trello.pojo.Card;
import com.progress.tracking.wrapper.trello.pojo.TrelloList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CourseSubmissionService {

    @Autowired
    private TrelloService trelloExec;

    public CourseToTrelloResponse submitToTrello(@RequestBody CourseToTrelloRequest req) {
        final Board board = this.trelloExec.searchBoardByName(req.getBoardName(), req.getBoardDescription(), req.getTrelloApiKey(), req.getTrelloApiToken());
        final TrelloList list = this.trelloExec.searchListFromBoard(board.getId(), req.getListName(), req.getTrelloApiKey(), req.getTrelloApiToken());
        final Card card = this.trelloExec.createTrelloCard(list.getId(), req.getCourse().getName(), req.getCourse().getDesc(), req.getTrelloApiKey(), req.getTrelloApiToken());
        this.trelloExec.attachLink(card.getId(), req.getCourse().getUrl(), req.getTrelloApiKey(), req.getTrelloApiToken());
        this.trelloExec.createChecklists(req.getCourse().getChapters(), card, req.getTrelloApiKey(), req.getTrelloApiToken());

        final CourseToTrelloResponse response = new CourseToTrelloResponse();
        response.setCardUrl(card.getUrl());
        return response;
    }

}
