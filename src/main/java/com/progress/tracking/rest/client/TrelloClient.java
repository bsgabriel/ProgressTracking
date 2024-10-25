package com.progress.tracking.rest.client;

import com.progress.tracking.wrapper.trello.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "trelloClient", url = "https://api.trello.com/1")
public interface TrelloClient {

    @GetMapping("/search")
    TrelloSearchResponse searchBoardByName(@RequestParam("query") String boardName,
                                           @RequestParam("token") String apiToken,
                                           @RequestParam("key") String apiKey);

    @PostMapping("/boards")
    Board createBoard(@RequestBody TrelloRequest trelloRequest);

    @GetMapping("/boards/{id}/lists")
    List<TrelloList> getListsFromBoard(@PathVariable("id") String boardId,
                                       @RequestParam("token") String apiToken,
                                       @RequestParam("key") String apiKey);

    @PostMapping("/list")
    TrelloList createList(@RequestBody TrelloRequest trelloRequest);

    @PostMapping("/cards")
    Card createCard(@RequestBody TrelloRequest trelloRequest);

    @PostMapping("/cards/{cardId}/attachments")
    CardAttachment addAttachment(@PathVariable("cardId") String cardId,
                                 @RequestBody TrelloRequest trelloRequest);

    @PostMapping("/checklists")
    Checklist createChecklist(@RequestBody TrelloRequest trelloRequest);

    @PostMapping("/checklists/{checklistId}/checkItems")
    ChecklistItem createChecklistItem(@PathVariable("checklistId") String checklistId,
                                      @RequestBody TrelloRequest trelloRequest);

}
