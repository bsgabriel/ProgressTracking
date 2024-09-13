package com.progress.tracking.rest.client;

import com.progress.tracking.rest.dto.udemy.UdemyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "udemyClient", url = "https://www.udemy.com/api-2.0")
public interface UdemyClient {

    @GetMapping("/courses")
    UdemyResponseDto searchCourse(@RequestHeader("Authorization") String Token,
                                  @RequestParam("search") String courseName,
                                  @RequestParam(value = "page_size", defaultValue = "5") Integer pageSize,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page);

    @GetMapping("/courses/{courseId}/public-curriculum-items/")
    UdemyResponseDto getCourseCurriculum(@RequestHeader("Authorization") String Token,
                                         @PathVariable(value = "courseId") Integer courseId,
                                         @RequestParam(value = "page_size", defaultValue = "100") Integer pageSize,
                                         @RequestParam(value = "page", defaultValue = "1") Integer page);
}
