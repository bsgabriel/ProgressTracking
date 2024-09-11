package com.progress.tracking.rest.client;

import com.progress.tracking.wrapper.udemy.pojo.UdemyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "udemyClient", url = "https://www.udemy.com/api-2.0")
public interface UdemyClient {

    @GetMapping("/courses")
    UdemyResponse searchCourse(@RequestHeader("Authorization") String Token,
                               @RequestParam("search") String courseName,
                               @RequestParam("page_size") Integer pageSize,
                               @RequestParam("page") Integer page);

}
