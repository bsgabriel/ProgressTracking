package com.progress.tracking;

import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.rest.mapper.CourseMapper;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseCurriculum;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
import com.progress.tracking.wrapper.trello.pojo.*;
import com.progress.tracking.wrapper.udemy.UdemyApiWrapper;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;

import java.util.List;

/**
 * Class created to test the integration of platforms.
 * To generate credentials for using the Trello API, refer to <a href="https://developer.atlassian.com/cloud/trello/guides/rest-api/api-introduction/">this link</a>.
 * To generate credentials for using the Udemy API, refer to <a href="https://www.udemy.com/user/edit-api-clients/">this link</a>.
 */
public class IntegrationTest {

    // credentials
    private static final String UDEMY_CLIENT_ID = "YOUR_UDEMY_CLIENT_ID";
    private static final String UDEMY_CLIENT_SECRET = "YOUR_UDEMY_CLIENT_SECRET";
    private static final String TRELLO_API_KEY = "YOUR_TRELLO_API_KEY";
    private static final String TRELLO_API_TOKEN = "YOUR_TRELLO_API_TOKEN";

    // data used for test
    private static final String BOARD_NAME = "Zero to Dev"; // The name of the board to be used
    private static final String BOARD_DESCRIPTION = "Board created via ProgressTracking."; // Description for the board
    private static final String LIST_NAME = "course stack"; // The name of the list that will hold the cards
    private static final String COURSE_TO_FIND = "Microsserviços do 0 com Spring Cloud, Spring Boot e Docker"; // Course to be searched

    private static TrelloApiWrapper tWrapper;
    private static UdemyApiWrapper uWrapper;

    public static UdemyApiWrapper getuWrapper() {
        if (uWrapper == null) {
            try {
                uWrapper = UdemyApiWrapper.initialize(UDEMY_CLIENT_ID, UDEMY_CLIENT_SECRET);
            } catch (InvalidParameterException e) {
                throw new RuntimeException(e);
            }
        }

        return uWrapper;
    }

    public static TrelloApiWrapper gettWrapper() {
        if (tWrapper == null) {
            try {
                tWrapper = TrelloApiWrapper.initialize(TRELLO_API_KEY, TRELLO_API_TOKEN);
            } catch (InvalidParameterException e) {
                throw new RuntimeException(e);
            }
        }

        return tWrapper;
    }

    public static void main(String[] args) {
        try {
            List<UdemyCourse> results = getuWrapper().searchCourse(COURSE_TO_FIND, 10, 1).getCourses();

            if (results.isEmpty()) {
                System.out.println("Couldn't find the specified course");
                return;
            }

            UdemyCourse uCourse = results.get(0);
            UdemyCourseCurriculum curriculum = getuWrapper().getCourseCurriculum(uCourse.getId(), 100);

            if (curriculum == null || curriculum.getChapters().isEmpty()) {
                System.out.println("Couldn't find the course curriculum");
                return;
            }

            Course course = new CourseMapper().udemyCourseToCourse(uCourse, curriculum);

            List<Board> boards = gettWrapper().searchBoardByName(BOARD_NAME);

            Board board;
            if (boards.isEmpty())
                board = createTrelloBoard(BOARD_NAME, BOARD_DESCRIPTION);
            else
                board = boards.get(0);

            TrelloList list = searchListFromBoard(board.getId(), LIST_NAME);
            Card card = createTrelloCard(list.getId(), course.getName(), course.getDesc());
            attachCourseLink(card.getId(), "Link", course.getUrl());

            int idxItem = 1;
            for (Result chapter : curriculum.getChapters().keySet()) {
                List<Result> lectures = curriculum.getChapters().get(chapter);
                if (lectures == null || lectures.isEmpty())
                    continue;

                Checklist checkList = creteChecklistForCard(card.getId(), chapter.getTitle());

                for (Result lecture : lectures) {
                    createItemForChecklist(checkList.getId(), idxItem + ". " + lecture.getTitle());
                    idxItem++;
                }
            }
        } catch (InvalidParameterException | ApiExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private static Board createTrelloBoard(String boardName, String desc) throws ApiExecutionException, InvalidParameterException {
        Board board = gettWrapper().createBoard(boardName, desc);

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

    private static TrelloList createTrelloList(final String idBoard, final String name) throws ApiExecutionException, InvalidParameterException {
        TrelloList list = gettWrapper().createList(idBoard, name);

        StringBuilder sb = new StringBuilder();
        sb.append("Created list:").append("\n");
        sb.append("\t").append("name: ").append(list.getName()).append("\n");
        sb.append("\t").append("id: ").append(list.getId()).append("\n");
        sb.append("\t").append("board's ID: ").append(list.getIdBoard()).append("\n");
        System.out.println(sb);

        return list;
    }

    private static Card createTrelloCard(String idList, String name, String desc) throws ApiExecutionException, InvalidParameterException {
        Card card = gettWrapper().createCard(idList, name, desc);

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

    private static void attachCourseLink(String idCard, String attName, String attUrl) throws ApiExecutionException, InvalidParameterException {
        CardAttachment att = gettWrapper().createCardUrlAttachment(idCard, attName, attUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("Anexo criado:").append("\n");
        sb.append("\t").append("id: ").append(att.getId()).append("\n");
        sb.append("\t").append("name: ").append(att.getName()).append("\n");
        sb.append("\t").append("url: ").append(att.getUrl()).append("\n");
        System.out.println(sb);
    }

    private static Checklist creteChecklistForCard(String idCard, String name) throws ApiExecutionException, InvalidParameterException {
        Checklist lst = gettWrapper().createChecklist(idCard, name);
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

    private static void createItemForChecklist(String idChecklist, String name) throws ApiExecutionException, InvalidParameterException {
        ChecklistItem item = gettWrapper().createChecklistItem(idChecklist, name);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist item:").append("\n");
        sb.append("\t").append("id: ").append(item.getId()).append("\n");
        sb.append("\t").append("name: ").append(item.getName()).append("\n");
        sb.append("\t").append("checklist's ID: ").append(item.getIdChecklist()).append("\n");
        System.out.println(sb);
    }

    private static TrelloList searchListFromBoard(final String idBoard, final String list) throws ApiExecutionException, InvalidParameterException {
        List<TrelloList> lists = gettWrapper().getListsFromBoard(idBoard);

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

}