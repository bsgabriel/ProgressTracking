package com.progress.tracking.wrapper.udemy;

import com.google.gson.Gson;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourseCurriculum;
import com.progress.tracking.util.WsUtil;
import com.progress.tracking.util.exception.ApiExecutionException;
import com.progress.tracking.util.exception.InvalidParameterException;
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
     * @param clientID     Udemy API client ID.
     * @param clientSecret Udemy API client secret.
     * @return UdemyApiWrapper instance.
     * @throws InvalidParameterException If either the clientID or clientSecret is null or blank.
     */
    public static UdemyApiWrapper initialize(final String clientID, final String clientSecret) throws InvalidParameterException {
        if (clientID == null || clientID.isBlank())
            throw new InvalidParameterException("clientID");

        if (clientSecret == null || clientSecret.isBlank())
            throw new InvalidParameterException("clientID");

        final String decoded = clientID + ":" + clientSecret;
        final UdemyApiWrapper wrapper = new UdemyApiWrapper();
        wrapper.token = Base64.getEncoder().encodeToString(decoded.getBytes());
        return wrapper;
    }

    /**
     * Creates the header with the necessary authorization information.
     *
     * @return Map representing the HTTP headers.
     */
    private Map<String, String> createHeader() {
        final Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Basic " + token);
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * Searches for Udemy courses based on the provided search string.
     *
     * @param search   Search string for courses.
     * @param pageSize The page size for the search results.
     * @return List of {@linkplain UdemyCourse} objects matching the search.
     * @throws InvalidParameterException If the search string is null or blank.
     * @throws InvalidParameterException If the page size is null or less than 5.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public List<UdemyCourse> searchCourse(final String search, final Integer pageSize) throws InvalidParameterException, ApiExecutionException {
        if (search == null || search.isBlank())
            throw new InvalidParameterException("search");

        if (pageSize == null || pageSize < 5)
            throw new InvalidParameterException("pageSize", "The value of 'pageSize' must not be null and must be greater than or equal to 5");

        final List<UdemyCourse> courses = new ArrayList<>();
        final Map<String, String> query = new HashMap<>();
        query.put("page_size", pageSize.toString());
        query.put("search", search);

        final UdemyResponse response;
        try {
            response = sendRequest(BASE_URL, query);
        } catch (Exception e) {
            throw new ApiExecutionException("An error occurred while searching for the specified course.", e);
        }

        for (final Result result : response.getResults()) {
            final UdemyCourse course = new UdemyCourse();
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
     * @param courseID Udemy course ID.
     * @param pageSize The page size for the search results.
     * @return {@linkplain UdemyCourseCurriculum} object representing the course curriculum.
     * @throws InvalidParameterException If the course ID is null.
     * @throws InvalidParameterException If the page size is null or less than 5.
     * @throws ApiExecutionException     If an error occurs during the API call.
     */
    public UdemyCourseCurriculum getCourseCurriculum(final Long courseID, final Integer pageSize) throws InvalidParameterException, ApiExecutionException {
        if (courseID == null)
            throw new InvalidParameterException("courseID");

        if (pageSize == null || pageSize < 5)
            throw new InvalidParameterException("pageSize", "The value of 'pageSize' must not be null and must be greater than or equal to 5");

        final List<Result> results = new ArrayList<>();
        final Map<String, String> query = new HashMap<>();
        query.put("page_size", pageSize.toString());

        String url = BASE_URL + courseID + "/public-curriculum-items/";

        do {
            final UdemyResponse response;
            try {
                response = sendRequest(url, query);
            } catch (Exception e) {
                throw new ApiExecutionException("An error occurred while searching for the curriculum for the specified course.", e);
            }

            if (response == null)
                throw new ApiExecutionException("Couldn't get a valid response while searching for course curriculum.");

            results.addAll(response.getResults());

            // Remove the "page_size" parameter from the query to prevent continuous concatenation.
            query.remove("page_size");

            url = response.getNext();
        } while (url != null && !url.trim().isEmpty());

        final UdemyCourseCurriculum course = new UdemyCourseCurriculum();
        course.setChapters(fromResultListToMap(results));
        return course;
    }

    /**
     * Converts a list of Udemy API results into a map representing chapters and their corresponding lectures.
     *
     * @param allResults List of Udemy API results
     * @return Map representing chapters and lectures
     */
    private Map<Result, List<Result>> fromResultListToMap(final List<Result> allResults) {
        final Map<Result, List<Result>> chapterLecturesMap = new LinkedHashMap<>();

        for (final Result result : allResults) {
            final String resultClass = result.getClass_();

            if ("chapter".equals(resultClass)) {
                chapterLecturesMap.put(result, new ArrayList<>());
                continue;
            }

            if ("lecture".equals(resultClass)) {
                final Result chapter = findChapter(allResults, result);
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
    private static Result findChapter(final List<Result> allResults, final Result lecture) {
        Result foundChapter = null;
        int minSortOrderDifference = Integer.MAX_VALUE;

        for (final Result result : allResults) {
            if ("chapter".equals(result.getClass_()) && result.getSortOrder() > lecture.getSortOrder()) {
                final int sortOrderDifference = result.getSortOrder() - lecture.getSortOrder();
                if (sortOrderDifference < minSortOrderDifference) {
                    minSortOrderDifference = sortOrderDifference;
                    foundChapter = result;
                }
            }
        }

        return foundChapter;
    }

    /**
     * Sends an HTTP GET request to the specified URL with the provided query parameters and retrieves the UdemyResponse.
     *
     * @param url   The URL to send the request to.
     * @param query The query parameters to include in the request.
     * @return The {@link UdemyResponse} object representing the API response.
     * @throws Exception If an error occurs during the HTTP GET request or JSON deserialization.
     */
    private UdemyResponse sendRequest(final String url, final Map<String, String> query) throws Exception {
        final String jsonResponse = WsUtil.sendGet(url, createHeader(), query);
        return gson.fromJson(jsonResponse, UdemyResponse.class);
    }

}
