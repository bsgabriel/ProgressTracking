package com.progress.tracking.rest.service;

import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperInitializationException;
import com.progress.tracking.util.exec.TrelloExec;
import com.progress.tracking.wrapper.trello.pojo.Board;
import com.progress.tracking.wrapper.trello.pojo.Card;
import com.progress.tracking.wrapper.trello.pojo.TrelloList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CourseSubmissionService {

    public ResponseEntity<CourseToTrelloResponse> submitToTrello(@RequestBody CourseToTrelloRequest req) {
        final CourseToTrelloResponse response = new CourseToTrelloResponse();

        try {
            final TrelloExec trelloExec = new TrelloExec(req);

            final Board board = trelloExec.searchBoardByName(req.getBoardName(), req.getBoardDescription());
            final TrelloList list = trelloExec.searchListFromBoard(board.getId(), req.getListName());
            final Card card = trelloExec.createTrelloCard(list.getId(), req.getCourse().getName(), req.getCourse().getDesc());
            trelloExec.attachCourseLink(card.getId(), "Link", req.getCourse().getUrl());
            trelloExec.createChecklists(req.getCourse().getChapters(), card);

        } catch (InvalidParameterException | ApiExecutionException | WrapperInitializationException e) {
            response.setError(e.getMessage());

            if (e instanceof InvalidParameterException)
                return ResponseEntity.badRequest().body(response);
            else
                return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

}
