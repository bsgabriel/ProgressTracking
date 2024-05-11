package com.progress.tracking.rest.service;

import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
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
        final TrelloApiWrapper tWrapper = TrelloApiWrapper.initialize(req.getTrelloApiKey(), req.getTrelloApiToken());
        final Board board = this.trelloExec.searchBoardByName(tWrapper, req.getBoardName(), req.getBoardDescription());
        final TrelloList list = this.trelloExec.searchListFromBoard(tWrapper, board.getId(), req.getListName());
        final Card card = this.trelloExec.createTrelloCard(tWrapper, list.getId(), req.getCourse().getName(), req.getCourse().getDesc());
        this.trelloExec.attachCourseLink(tWrapper, card.getId(), "Link", req.getCourse().getUrl());
        this.trelloExec.createChecklists(tWrapper, req.getCourse().getChapters(), card);

        final CourseToTrelloResponse response = new CourseToTrelloResponse();
        response.setCardUrl(card.getUrl());
        return response;
    }

}
