package com.progress.tracking.rest.service;

import com.progress.tracking.rest.client.TrelloClient;
import com.progress.tracking.rest.dto.ChapterDTO;
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
        return trelloClient.searchBoardByName(boardName, apiToken, apiKey).getBoards()
                .stream()
                .findFirst()
                .orElse(trelloClient.createBoard(TrelloRequest.builder()
                        .name(boardName)
                        .description(description)
                        .apiKey(apiKey)
                        .apiToken(apiToken)
                        .defaultLists(false)
                        .defaultLabels(false)
                        .build()));
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

    public CardAttachment attachLink(String cardId, String url, String apiKey, String apiToken) {
        return trelloClient.addAttachment(cardId, TrelloRequest.builder()
                .url(url)
                .name("Link")
                .apiKey(apiKey)
                .apiToken(apiToken)
                .build());
    }

    private Checklist createChecklistForCard(String idCard, String name, String apiKey, String apiToken) {
        return trelloClient.createChecklist(TrelloRequest.builder()
                .idCard(idCard)
                .name(name)
                .apiKey(apiKey)
                .apiToken(apiToken)
                .build());
    }

    private void createItemForChecklist(String idChecklist, final String name, final Integer idx, String apiKey, String apiToken) {
        trelloClient.createChecklistItem(idChecklist, TrelloRequest.builder()
                .position(idx)
                .name(name)
                .apiKey(apiKey)
                .apiToken(apiToken)
                .build());
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

    public void createChecklists(List<ChapterDTO> chapters, Card card, String apiKey, String apiToken) {
        for (ChapterDTO chapter : chapters) {
            final List<String> lessons = chapter.getLessons();
            if (lessons == null || lessons.isEmpty())
                continue;

            final Checklist checkList = this.createChecklistForCard(card.getId(), chapter.getName(), apiKey, apiToken);
            final int lessonsPerThread = (int) Math.ceil((double) lessons.size() / MAX_THREADS);
            final CountDownLatch latch = new CountDownLatch(MAX_THREADS);

            for (int threadId = 0; threadId < MAX_THREADS; threadId++) {
                final int start = threadId * lessonsPerThread;
                final int end = Math.min((threadId + 1) * lessonsPerThread, lessons.size());
                this.taskExecutor.submit(() -> {
                    try {
                        for (int lectureIndex = start; lectureIndex < end; lectureIndex++) {
                            String lecture = lessons.get(lectureIndex);
                            this.createItemForChecklist(checkList.getId(), lecture, lectureIndex + 1, apiKey, apiToken);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("Error while creating checklist item", e);
            }
        }
    }
}
