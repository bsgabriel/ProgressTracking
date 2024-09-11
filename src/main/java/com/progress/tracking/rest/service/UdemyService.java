package com.progress.tracking.rest.service;

import com.progress.tracking.rest.client.UdemyClient;
import com.progress.tracking.rest.request.SearchCourseRequest;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseSearch;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class UdemyService {

    @Autowired
    private UdemyClient udemyClient;

    public UdemyCourseSearch searchCourse(SearchCourseRequest req) {
        if (StringUtils.isBlank(req.getCourse()))
            throw new InvalidParameterException("search");

        if (req.getMax() == null || req.getMax() < 5)
            throw new InvalidParameterException("pageSize", "The value of 'pageSize' must not be null and must be greater than or equal to 5");

        if (req.getPage() == null || req.getPage() < 1)
            throw new InvalidParameterException("page", "The value of 'page' must not be null and must be greater than or equal to 1");

        var token = this.generateToken(req.getUdemyClientId(), req.getUdemyClientSecret());
        var response = this.udemyClient.searchCourse(token, req.getCourse(), req.getMax(), req.getPage());

        return UdemyCourseSearch.builder()
                .currentPage(req.getPage())
                .count(response.getCount())
                .nextPage(isNotBlank(response.getNext()) ? req.getPage() + 1 : null)
                .previousPage(isNotBlank(response.getPrevious()) ? req.getPage() - 1 : null)
                .courses(createCourseList(response.getResults()))
                .build();

    }

    private String generateToken(String clientId, String clientSecret) {
        var decoded = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(decoded.getBytes());
    }

    private List<UdemyCourse> createCourseList(List<Result> results) {
        return results.stream()
                .map(this::resultToCourse)
                .toList();

    }

    private UdemyCourse resultToCourse(Result result) {
        return UdemyCourse.builder()
                .id(result.getId())
                .title(result.getTitle())
                .headline(result.getHeadline())
                .url(isNotBlank(result.getUrl()) ? "https://www.udemy.com" + result.getUrl() : null)
                .instructors(result.getVisibleInstructors())
                .image(result.getImage())
                .build();
    }

}
