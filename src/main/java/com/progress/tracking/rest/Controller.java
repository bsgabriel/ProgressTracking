package com.progress.tracking.rest;

import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.rest.request.AbstractRequest;
import com.progress.tracking.rest.request.CourseToTrelloRequest;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.rest.response.CourseToTrelloResponse;
import com.progress.tracking.rest.response.SearchCourseResponse;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.util.exception.WrapperInitializationException;
import com.progress.tracking.util.exec.TrelloExec;
import com.progress.tracking.wrapper.trello.pojo.Board;
import com.progress.tracking.wrapper.trello.pojo.Card;
import com.progress.tracking.wrapper.trello.pojo.Checklist;
import com.progress.tracking.wrapper.trello.pojo.TrelloList;
import com.progress.tracking.wrapper.udemy.UdemyApiWrapper;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseCurriculum;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseSearch;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @PostMapping("searchCourse")
    public ResponseEntity<SearchCourseResponse> searchCourse(@RequestBody SearchCourseRequest req) {
        final UdemyApiWrapper uWrapper;
        final SearchCourseResponse response = new SearchCourseResponse();

        try {
            uWrapper = initUdemyWrapper(req);
        } catch (WrapperInitializationException e) {
            response.setError(e.getMessage());
            if (e.getCause() != null)
                response.setDetailedMessage(e.getCause().getMessage());

            return ResponseEntity.internalServerError().body(response);
        }

        final UdemyCourseSearch ret;
        try {
            final Integer page = req.getPage() != null ? req.getPage() : 1;
            ret = uWrapper.searchCourse(req.getCourse(), req.getMax(), page);
        } catch (InvalidParameterException e) {
            response.setError("Error while searching for course");
            response.setDetailedMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (ApiExecutionException e) {
            response.setError(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        if (ret.getCourses().isEmpty()) {
            response.setMessage("Couldn't find the specified course");
            return ResponseEntity.status(404).body(response);
        }

        for (UdemyCourse c : ret.getCourses()) {
            final Course courseInfo = new Course();
            courseInfo.setId(c.getId());
            courseInfo.setName(c.getTitle());
            courseInfo.setDesc(createCourseDescription(c.getHeadline(), c.getInstructors()));
            courseInfo.setImage(c.getImage());
            courseInfo.setUrl(c.getUrl());
            response.getCourses().add(courseInfo);
        }

        response.setCurrentPage(ret.getCurrentPage());
        response.setPrevisouPage(ret.getPreviousPage());
        response.setNextPage(ret.getNextPage());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("courseToTrello")
    public ResponseEntity<CourseToTrelloResponse> courseToTrello(@RequestBody CourseToTrelloRequest req) {
        final CourseToTrelloResponse response = new CourseToTrelloResponse();
        final UdemyApiWrapper uWrapper;
        final TrelloExec trelloExec;

        try {
            uWrapper = initUdemyWrapper(req);
            trelloExec = new TrelloExec(req);

            final UdemyCourseCurriculum curriculum = uWrapper.getCourseCurriculum(req.getCourseId(), 100);
            final Board board = trelloExec.searchBoardByName(req.getBoardName(), req.getBoardDescription());
            final TrelloList list = trelloExec.searchListFromBoard(board.getId(), req.getListName());
            final Card card = trelloExec.createTrelloCard(list.getId(), req.getCourseName(), req.getCourseDescription());
            trelloExec.attachCourseLink(card.getId(), "Link", req.getCourseLink());

            int idxItem = 1;
            for (Result chapter : curriculum.getChapters().keySet()) {
                final List<Result> lectures = curriculum.getChapters().get(chapter);
                if (lectures == null || lectures.isEmpty())
                    continue;

                final Checklist checkList = trelloExec.createChecklistForCard(card.getId(), chapter.getTitle());

                for (Result lecture : lectures) {
                    trelloExec.createItemForChecklist(checkList.getId(), idxItem + ". " + lecture.getTitle());
                    idxItem++;
                }
            }

        } catch (InvalidParameterException | ApiExecutionException | WrapperInitializationException e) {
            response.setError(e.getMessage());

            if (e instanceof InvalidParameterException)
                return ResponseEntity.badRequest().body(response);
            else
                return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok().body(response);
    }

    private static UdemyApiWrapper initUdemyWrapper(final AbstractRequest req) throws WrapperInitializationException {
        try {
            return UdemyApiWrapper.initialize(req.getUdemyClientId(), req.getUdemyClientSecret());
        } catch (InvalidParameterException e) {
            throw new WrapperInitializationException("Couldn't initialize Udemy's API wrapper", e);
        }
    }

    private static String createCourseDescription(String desc, List<VisibleInstructor> instructors) {
        if (instructors == null || instructors.isEmpty())
            return desc;

        final StringBuilder sb = new StringBuilder();
        sb.append(desc);
        sb.append("\n\n");
        sb.append(instructors.size() > 1 ? "Instructors: " : "Instructor: ");
        for (VisibleInstructor instructor : instructors) {
            sb.append(instructor.getDisplayName());
            if (instructors.indexOf(instructor) != instructors.size() - 1)
                sb.append(", ");
        }

        return sb.toString();
    }
}
