package com.progress.tracking.util.exec;

import com.progress.tracking.rest.entity.Chapter;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
import com.progress.tracking.wrapper.trello.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class TrelloExec {

    private static final int MAX_THREADS = 5;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Board searchBoardByName(final TrelloApiWrapper tWrapper, final String boardName, final String description) throws ApiExecutionException, InvalidParameterException {
        final List<Board> boards = tWrapper.searchBoardByName(boardName);
        final Board board;
        if (boards.isEmpty())
            board = this.createTrelloBoard(tWrapper, boardName, description);
        else
            board = boards.get(0);

        return board;
    }

    private Board createTrelloBoard(final TrelloApiWrapper tWrapper, final String boardName, final String desc) throws ApiExecutionException, InvalidParameterException {
        final Board board = tWrapper.createBoard(boardName, desc);

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

    private TrelloList createTrelloList(final TrelloApiWrapper tWrapper, final String idBoard, final String name) throws ApiExecutionException, InvalidParameterException {
        TrelloList list = tWrapper.createList(idBoard, name);

        StringBuilder sb = new StringBuilder();
        sb.append("Created list:").append("\n");
        sb.append("\t").append("name: ").append(list.getName()).append("\n");
        sb.append("\t").append("id: ").append(list.getId()).append("\n");
        sb.append("\t").append("board's ID: ").append(list.getIdBoard()).append("\n");
        System.out.println(sb);

        return list;
    }

    public Card createTrelloCard(final TrelloApiWrapper tWrapper, final String idList, final String name, final String desc) throws ApiExecutionException, InvalidParameterException {
        Card card = tWrapper.createCard(idList, name, desc);

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

    public void attachCourseLink(final TrelloApiWrapper tWrapper, final String idCard, final String attName, final String attUrl) throws ApiExecutionException, InvalidParameterException {
        CardAttachment att = tWrapper.createCardUrlAttachment(idCard, attName, attUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("Anexo criado:").append("\n");
        sb.append("\t").append("id: ").append(att.getId()).append("\n");
        sb.append("\t").append("name: ").append(att.getName()).append("\n");
        sb.append("\t").append("url: ").append(att.getUrl()).append("\n");
        System.out.println(sb);
    }

    private Checklist createChecklistForCard(final TrelloApiWrapper tWrapper, final String idCard, final String name) throws ApiExecutionException, InvalidParameterException {
        Checklist lst = tWrapper.createChecklist(idCard, name);
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

    private void createItemForChecklist(final TrelloApiWrapper tWrapper, final String idChecklist, final String name, final Integer idx) throws ApiExecutionException, InvalidParameterException {
        ChecklistItem item = tWrapper.createChecklistItem(idChecklist, name, idx);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist item:").append("\n");
        sb.append("\t").append("id: ").append(item.getId()).append("\n");
        sb.append("\t").append("name: ").append(item.getName()).append("\n");
        sb.append("\t").append("checklist's ID: ").append(item.getIdChecklist()).append("\n");
        sb.append("\t").append("index: ").append(idx).append("\n");
        System.out.println(sb);
    }

    public TrelloList searchListFromBoard(final TrelloApiWrapper tWrapper, final String idBoard, final String list) throws ApiExecutionException, InvalidParameterException {
        List<TrelloList> lists = tWrapper.getListsFromBoard(idBoard);

        if (lists == null || lists.isEmpty())
            return createTrelloList(tWrapper, idBoard, list);

        TrelloList tList = null;
        for (TrelloList l : lists) {
            if (l.getName().equalsIgnoreCase(list)) {
                tList = l;
                break;
            }
        }

        if (tList == null)
            return createTrelloList(tWrapper, idBoard, list);

        return tList;
    }

    public void createChecklists(final TrelloApiWrapper tWrapper, final List<Chapter> chapters, final Card card) throws ApiExecutionException, InvalidParameterException {
        for (Chapter chapter : chapters) {
            final List<String> lessons = chapter.getLessons();
            if (lessons == null || lessons.isEmpty())
                continue;

            final Checklist checkList = this.createChecklistForCard(tWrapper, card.getId(), chapter.getName());
            final int lessonsPerThread = (int) Math.ceil((double) lessons.size() / MAX_THREADS);
            final CountDownLatch latch = new CountDownLatch(MAX_THREADS);

            for (int threadId = 0; threadId < MAX_THREADS; threadId++) {
                final int start = threadId * lessonsPerThread;
                final int end = Math.min((threadId + 1) * lessonsPerThread, lessons.size());
                this.taskExecutor.submit(() -> submit(start, end, tWrapper, latch, checkList.getId(), lessons));
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void submit(final int start, final int end, final TrelloApiWrapper tWrapper, final CountDownLatch latch, final String checkListId, final List<String> lessons) {
        try {
            for (int lectureIndex = start; lectureIndex < end; lectureIndex++) {
                String lecture = lessons.get(lectureIndex);
                this.createItemForChecklist(tWrapper, checkListId, lecture, lectureIndex + 1);
            }
        } catch (InvalidParameterException | ApiExecutionException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}
