package com.progress.tracking.wrapper.udemy;

import com.google.gson.Gson;
import com.progress.tracking.entity.UdemyCourse;
import com.progress.tracking.entity.UdemyCourseCurriculum;
import com.progress.tracking.util.WsUtil;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import com.progress.tracking.wrapper.udemy.pojo.UdemyResponse;

import java.util.*;

/**
 * UdemyApiWrapper is a utility class for interacting with the Udemy API to retrieve course information.
 * For more details on the Udemy API, refer to the <a href="https://www.udemy.com/developers/affiliate/">official documentation</a>.
 */
public class UdemyApiWrapper {
    private static final String BASE_URL = "https://www.udemy.com/api-2.0/courses/";
    private static final Gson gson = new Gson();
    private String token;

    private UdemyApiWrapper() {
    }

    /**
     * Initializes the Wrapper, generating the token with the provided information.
     *
     * @param clientID     Udemy API client ID
     * @param clientSecret Udemy API client secret
     * @return UdemyApiWrapper instance
     */
    public static UdemyApiWrapper initialize(String clientID, String clientSecret) {
        UdemyApiWrapper wrapper = new UdemyApiWrapper();
        String decoded = clientID + ":" + clientSecret;
        wrapper.token = Base64.getEncoder().encodeToString(decoded.getBytes());
        return wrapper;
    }

    /**
     * Creates the header with the necessary authorization information.
     *
     * @return Map representing the HTTP headers
     */
    private Map<String, String> createHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic " + token);
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * Searches for Udemy courses based on the provided search string.
     *
     * @param search Search string for courses
     * @return List of UdemyCourse objects matching the search
     */
    public List<UdemyCourse> searchCourse(String search) {
        String jsonResponse;

        Map<String, String> query = new HashMap<>();
        List<UdemyCourse> courses = new ArrayList<>();

        // TODO create param for page_size
        query.put("page_size", "5");
        query.put("search", search);
        try {
            jsonResponse = WsUtil.sendGet(BASE_URL, createHeader(), query);
        } catch (Exception e) {
            // TODO: log
            e.printStackTrace();
            return courses;
        }

        UdemyResponse response = gson.fromJson(jsonResponse, UdemyResponse.class);

        for (Result result : response.getResults()) {
            UdemyCourse course = new UdemyCourse();
            course.setId(result.getId());
            course.setTitle(result.getTitle());
            course.setHeadline(result.getHeadline());
            course.setUrl(result.getUrl());
            course.setInstructors(result.getVisibleInstructors());
            courses.add(course);
        }

        return courses;
    }

    /**
     * Retrieves the curriculum of a Udemy course based on the provided course ID.
     *
     * @param courseID Udemy course ID
     * @return UdemyCourseCurriculum object representing the course curriculum
     */
    public UdemyCourseCurriculum getCourseCurriculum(Long courseID) {
        // TODO: throw exception if token is null
        // TODO: throw exception if courseID is null

        String url = BASE_URL + courseID.toString() + "/public-curriculum-items/";
        String jsonResponse;

        Map<String, String> query = new HashMap<>();

        // TODO create param for page_size
        query.put("page_size", "100");
        try {
            jsonResponse = WsUtil.sendGet(url, createHeader(), query);
        } catch (Exception e) {
            // TODO: log
            e.printStackTrace();
            return null;
        }

        UdemyResponse response = gson.fromJson(jsonResponse, UdemyResponse.class);

        List<Result> results = new ArrayList<>(response.getResults());

        while (response.getNext() != null && !response.getNext().trim().isBlank()) {
            try {
                jsonResponse = WsUtil.sendGet(response.getNext(), createHeader(), null);
            } catch (Exception e) {
                // TODO: log
                e.printStackTrace();
                return null;
            }
            response = gson.fromJson(jsonResponse, UdemyResponse.class);
            results.addAll(response.getResults());
        }

        UdemyCourseCurriculum course = new UdemyCourseCurriculum();
        course.setChapters(fromResultListToMap(results));
        return course;
    }

    /**
     * Converts a list of Udemy API results into a map representing chapters and their corresponding lectures.
     *
     * @param allResults List of Udemy API results
     * @return Map representing chapters and lectures
     */
    private Map<Result, List<Result>> fromResultListToMap(List<Result> allResults) {
        Map<Result, List<Result>> chapterLecturesMap = new LinkedHashMap<>();

        for (Result result : allResults) {
            String resultClass = result.getClass_();

            if ("chapter".equals(resultClass)) {
                chapterLecturesMap.put(result, new ArrayList<>());
                continue;
            }

            if ("lecture".equals(resultClass)) {
                Result chapter = findChapter(allResults, result);
                if (chapter != null && chapterLecturesMap.containsKey(chapter)) {
                    chapterLecturesMap.get(chapter).add(result);
                }
            }
        }

        return chapterLecturesMap;
    }

    /**
     * Finds the chapter corresponding to a given lecture within a list of Udemy API results.
     *
     * @param allResults List of Udemy API results
     * @param lecture    Udemy API result representing a lecture
     * @return Udemy API result representing the corresponding chapter
     */
    private static Result findChapter(List<Result> allResults, Result lecture) {
        Result foundChapter = null;
        int minSortOrderDifference = Integer.MAX_VALUE;

        for (Result result : allResults) {
            if ("chapter".equals(result.getClass_()) && result.getSortOrder() > lecture.getSortOrder()) {
                int sortOrderDifference = result.getSortOrder() - lecture.getSortOrder();
                if (sortOrderDifference < minSortOrderDifference) {
                    minSortOrderDifference = sortOrderDifference;
                    foundChapter = result;
                }
            }
        }

        return foundChapter;
    }
}
