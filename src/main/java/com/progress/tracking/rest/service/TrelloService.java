package com.progress.tracking.rest.service;

import com.progress.tracking.rest.client.TrelloClient;
import com.progress.tracking.rest.dto.ChapterDTO;
import com.progress.tracking.wrapper.trello.TrelloApiWrapper;
import com.progress.tracking.wrapper.trello.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class TrelloService {

    private static final int MAX_THREADS = 5;

    @Autowired
    private TrelloClient trelloClient;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Board searchBoardByName(String boardName, String description, String apiKey, String apiToken) {
        var boards = trelloClient.searchBoardByName(boardName, apiToken, apiKey).getBoards();
        return !boards.isEmpty() ? boards.get(0) : this.createTrelloBoard(boardName, description, apiKey, apiToken);
    }

    private Board createTrelloBoard(String boardName, String desc, String apiKey, String apiToken) {
        return trelloClient.createBoard(TrelloRequest.builder()
                .name(boardName)
                .description(desc)
                .apiKey(apiKey)
                .apiToken(apiToken)
                .defaultLists(false)
                .defaultLabels(false)
                .build());
    }

    public Card createTrelloCard(String idList, String cardName, String desc, String apiKey, String apiToken) {
        return trelloClient.createCard(TrelloRequest.builder()
                .idList(idList)
                .name(cardName)
                .description(desc)
                .apiToken(apiToken)
                .apiKey(apiKey)
                .build());
    }

    public void attachCourseLink(final TrelloApiWrapper tWrapper, final String idCard, final String attName, final String attUrl) {
        CardAttachment att = tWrapper.createCardUrlAttachment(idCard, attName, attUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("Attatchment created:").append("\n");
        sb.append("\t").append("id: ").append(att.getId()).append("\n");
        sb.append("\t").append("name: ").append(att.getName()).append("\n");
        sb.append("\t").append("url: ").append(att.getUrl());
        log.info(sb.toString());
    }

    private Checklist createChecklistForCard(final TrelloApiWrapper tWrapper, final String idCard, final String name) {
        Checklist lst = tWrapper.createChecklist(idCard, name);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist created:").append("\n");
        sb.append("\t").append("id: ").append(lst.getId()).append("\n");
        sb.append("\t").append("name: ").append(lst.getName()).append("\n");
        sb.append("\t").append("card's ID: ").append(lst.getIdCard()).append("\n");
        sb.append("\t").append("board's ID: ").append(lst.getIdBoard()).append("\n");
        sb.append("\t").append("items: ").append(lst.getItems());
        log.info(sb.toString());

        return lst;
    }

    private void createItemForChecklist(final TrelloApiWrapper tWrapper, final String idChecklist, final String name, final Integer idx) {
        ChecklistItem item = tWrapper.createChecklistItem(idChecklist, name, idx);
        StringBuilder sb = new StringBuilder();
        sb.append("Checklist item created:").append("\n");
        sb.append("\t").append("id: ").append(item.getId()).append("\n");
        sb.append("\t").append("name: ").append(item.getName()).append("\n");
        sb.append("\t").append("checklist's ID: ").append(item.getIdChecklist()).append("\n");
        sb.append("\t").append("index: ").append(idx);
        log.info(sb.toString());
    }

    public TrelloList searchListFromBoard(String idBoard, String listName, String apiKey, String apiToken) {
        return trelloClient.getListsFromBoard(idBoard, apiKey, apiToken)
                .stream()
                .filter(list -> list.getName().equalsIgnoreCase(listName))
                .findFirst()
                .orElse(trelloClient.createList(TrelloRequest.builder()
                        .name(listName)
                        .idBoard(idBoard)
                        .apiKey(apiKey)
                        .apiToken(apiToken)
                        .build()));

    }

    public void createChecklists(final TrelloApiWrapper tWrapper, final List<ChapterDTO> chapters, final Card card) {
        for (ChapterDTO chapter : chapters) {
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
                log.error("Error while creating checklist item", e);
            }
        }
    }

    private void submit(final int start, final int end, final TrelloApiWrapper tWrapper, final CountDownLatch latch, final String checkListId, final List<String> lessons) {
        try {
            for (int lectureIndex = start; lectureIndex < end; lectureIndex++) {
                String lecture = lessons.get(lectureIndex);
                this.createItemForChecklist(tWrapper, checkListId, lecture, lectureIndex + 1);
            }
        } finally {
            latch.countDown();
        }
    }
}
