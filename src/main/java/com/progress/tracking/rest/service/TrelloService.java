package com.progress.tracking.rest.service;

import com.progress.tracking.rest.client.TrelloClient;
import com.progress.tracking.rest.dto.ChapterDTO;
import com.progress.tracking.rest.dto.trello.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

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
                .orElseGet(() -> trelloClient.createBoard(TrelloRequest.builder()
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
        return trelloClient.getListsFromBoard(idBoard, apiToken, apiKey)
                .stream()
                .filter(list -> list.getName().equalsIgnoreCase(listName))
                .findFirst()
                .orElseGet(() -> trelloClient.createList(TrelloRequest.builder()
                        .name(listName)
                        .idBoard(idBoard)
                        .apiKey(apiKey)
                        .apiToken(apiToken)
                        .build()));

    }

    public void createChecklists(List<ChapterDTO> chapters, Card card, String apiKey, String apiToken) {
        for (ChapterDTO chapter : chapters) {
            List<String> lessons = chapter.getLessons();
            if (lessons == null || lessons.isEmpty())
                continue;

            Checklist checklist = this.createChecklistForCard(card.getId(), chapter.getName(), apiKey, apiToken);
            int totalLessons = lessons.size();
            int threadCount = Math.min(totalLessons, MAX_THREADS);
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

            for (int i = 0; i < totalLessons; i++) {
                String lesson = lessons.get(i);
                int lessonIndex = i + 1;

                completionService.submit(() -> {
                    try {
                        this.createItemForChecklist(checklist.getId(), lesson, lessonIndex, apiKey, apiToken);
                    } catch (Exception e) {
                        log.error("Failed to create checklist item for lesson: {} (index: {})", lesson, lessonIndex, e);
                        throw e;
                    }
                    return null;
                });
            }

            for (int i = 0; i < totalLessons; i++) {
                try {
                    completionService.take().get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while processing checklist items", e);
                }
            }

            executor.shutdown();
        }
    }
}