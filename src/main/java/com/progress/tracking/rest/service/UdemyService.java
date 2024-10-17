package com.progress.tracking.rest.service;

import com.progress.tracking.rest.client.UdemyClient;
import com.progress.tracking.util.exception.InvalidParameterException;
import com.progress.tracking.rest.dto.udemy.UdemyCourse;
import com.progress.tracking.rest.dto.udemy.UdemyCourseSearch;
import com.progress.tracking.rest.dto.udemy.UdemyResultDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class UdemyService {

    @Autowired
    private UdemyClient udemyClient;

    public UdemyCourseSearch searchCourse(String clientId, String clientSecret, String courseName, Integer pageSize, Integer page) {
        if (StringUtils.isBlank(courseName))
            throw new InvalidParameterException("search");

        var token = this.generateToken(clientId, clientSecret);
        var response = this.udemyClient.searchCourse(token, courseName, pageSize, page);

        return UdemyCourseSearch.builder()
                .currentPage(page)
                .count(response.getCount())
                .nextPage(isNotBlank(response.getNext()) ? page + 1 : null)
                .previousPage(isNotBlank(response.getPrevious()) ? page - 1 : null)
                .courses(createCourseList(response.getResults()))
                .build();

    }

    public Map<UdemyResultDto, List<UdemyResultDto>> getCourseCurriculum(String clientID, String clientSecret, Integer courseID, Integer pageSize) throws InvalidParameterException {
        if (courseID == null) {
            throw new InvalidParameterException("courseID");
        }

        if (pageSize == null || pageSize < 5) {
            throw new InvalidParameterException("pageSize", "The value of 'pageSize' must not be null and must be greater than or equal to 5");
        }

        List<UdemyResultDto> results = new ArrayList<>();
        int page = 1;
        String nextUrl;
        var token = this.generateToken(clientID, clientSecret);

        do {
            var response = udemyClient.getCourseCurriculum(token, courseID, pageSize, page);
            results.addAll(response.getResults());
            nextUrl = response.getNext();
            page++;
        } while (isNotBlank(nextUrl));

        return convertResultListToMap(results);
    }

    private String generateToken(String clientId, String clientSecret) {
        var decoded = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(decoded.getBytes());
    }

    private List<UdemyCourse> createCourseList(List<UdemyResultDto> results) {
        return results.stream()
                .map(this::resultToCourse)
                .toList();

    }

    private UdemyCourse resultToCourse(UdemyResultDto result) {
        return UdemyCourse.builder()
                .id(result.getId())
                .title(result.getTitle())
                .headline(result.getHeadline())
                .url(isNotBlank(result.getUrl()) ? "https://www.udemy.com" + result.getUrl() : null)
                .instructors(result.getVisibleInstructors())
                .image(result.getImage())
                .build();
    }

    private Map<UdemyResultDto, List<UdemyResultDto>> convertResultListToMap(List<UdemyResultDto> allResults) {
        Map<UdemyResultDto, List<UdemyResultDto>> chapterLecturesMap = allResults.stream()
                .filter(result -> "chapter".equals(result.getType()))
                .collect(Collectors.toMap(
                        result -> result,
                        result -> new ArrayList<>(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        allResults.stream()
                .filter(result -> "lecture".equals(result.getType()))
                .forEach(lecture -> {
                    UdemyResultDto chapter = findChapter(allResults, lecture);
                    if (nonNull(chapter) && chapterLecturesMap.containsKey(chapter))
                        chapterLecturesMap.get(chapter).add(lecture);

                });
        return chapterLecturesMap;
    }

    private UdemyResultDto findChapter(final List<UdemyResultDto> allResults, final UdemyResultDto lecture) {
        return allResults.stream()
                .filter(result -> "chapter".equals(result.getType()) && result.getSortOrder() > lecture.getSortOrder())
                .min(Comparator.comparingInt(result -> result.getSortOrder() - lecture.getSortOrder()))
                .orElse(null);
    }

}
