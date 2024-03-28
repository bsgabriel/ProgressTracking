package com.progress.tracking.rest.service;

import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exec.TrelloExec;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
import com.progress.tracking.wrapper.trello.pojo.Board;
import com.progress.tracking.wrapper.trello.pojo.Card;
import com.progress.tracking.wrapper.trello.pojo.TrelloList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CourseSubmissionService {

    @Autowired
    private TrelloExec trelloExec;

    public ResponseEntity<CourseToTrelloResponse> submitToTrello(@RequestBody CourseToTrelloRequest req) {
        final CourseToTrelloResponse response = new CourseToTrelloResponse();

        try {
            final TrelloApiWrapper tWrapper = TrelloApiWrapper.initialize(req.getTrelloApiKey(), req.getTrelloApiToken());
            final Board board = this.trelloExec.searchBoardByName(tWrapper, req.getBoardName(), req.getBoardDescription());
            final TrelloList list = this.trelloExec.searchListFromBoard(tWrapper, board.getId(), req.getListName());
            final Card card = this.trelloExec.createTrelloCard(tWrapper, list.getId(), req.getCourse().getName(), req.getCourse().getDesc());
            this.trelloExec.attachCourseLink(tWrapper, card.getId(), "Link", req.getCourse().getUrl());
            this.trelloExec.createChecklists(tWrapper, req.getCourse().getChapters(), card);
        } catch (InvalidParameterException | ApiExecutionException e) {
            response.setError(e.getMessage());

            if (e instanceof InvalidParameterException)
                return ResponseEntity.badRequest().body(response);
            else
                return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

}
