package com.progress.tracking.rest.client;

import com.progress.tracking.wrapper.trello.pojo.Board;
import com.progress.tracking.wrapper.trello.pojo.TrelloList;
import com.progress.tracking.wrapper.trello.pojo.TrelloRequest;
import com.progress.tracking.wrapper.trello.pojo.TrelloSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "trelloClient", url = "https://api.trello.com/1")
public interface TrelloClient {

    @GetMapping("/search")
    TrelloSearchResponse searchBoardByName(@RequestParam("query") String boardName,
                                           @RequestParam("token") String apiToken,
                                           @RequestParam("key") String apiKey);

    @PostMapping("/boards")
    Board createBoard(@RequestBody TrelloRequest trelloRequest);

    @GetMapping("/list")
    List<TrelloList> getListsFromBoard(@RequestParam("boardId") String boardId,
                                       @RequestParam("token") String apiToken,
                                       @RequestParam("key") String apiKey);

    @PostMapping("/list")
    TrelloList createList(@RequestBody TrelloRequest trelloRequest);
}
