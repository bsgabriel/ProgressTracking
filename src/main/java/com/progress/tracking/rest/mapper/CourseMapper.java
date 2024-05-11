package com.progress.tracking.rest.mapper;

import com.progress.tracking.rest.entity.Chapter;
import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for mapping courses from different platforms to the {@linkplain Course} entity.
 */
@Service
public class CourseMapper {

    /**
     * Maps a {@linkplain UdemyCourse} object to a {@linkplain Course} object, including the chapters and lessons from the Udemy course curriculum.
     *
     * @param udemyCourse           The {@linkplain UdemyCourse} object to map.
     * @param chapters The {@linkplain Map} object containing the chapters and lessons for the Udemy course.
     * @return A Course object mapped from the {@linkplain UdemyCourse}.
     */
    public Course courseFromUdemy(final UdemyCourse udemyCourse, final Map<Result, List<Result>> chapters) {
        if (udemyCourse == null)
            return null;

        final Course course = new Course();
        course.setId(udemyCourse.getId());
        course.setName(udemyCourse.getTitle());
        course.setDesc(createCourseDescription(udemyCourse.getHeadline(), udemyCourse.getInstructors()));
        course.setImage(udemyCourse.getImage());
        course.setUrl(udemyCourse.getUrl());
        course.setChapters(createChapetrsList(chapters));

        return course;
    }

    /**
     * Converts the {@linkplain Map} into a list of chapters for the {@linkplain Course}.
     * Each chapter contains its name and a list of lessons.
     *
     * @param uChapters A map representing Udemy course chapters and their corresponding lectures.
     * @return A list of {@linkplain Chapter} objects representing the chapters of the course.
     */
    private List<Chapter> createChapetrsList(final Map<Result, List<Result>> uChapters) {
        final List<Chapter> chapters = new ArrayList<>();
        if (uChapters == null || uChapters.isEmpty())
            return chapters;

        int idxItem = 1;
        for (Result uChapter : uChapters.keySet()) {
            final List<Result> lectures = uChapters.get(uChapter);
            if (lectures == null || lectures.isEmpty())
                continue;

            final Chapter chapter = new Chapter();
            chapter.setName(uChapter.getTitle());

            for (Result lecture : lectures) {
                chapter.getLessons().add(idxItem + ". " + lecture.getTitle());
                idxItem++;
            }

            chapters.add(chapter);
        }

        return chapters;
    }

    /**
     * Creates the course description based on the original description and list of {@linkplain VisibleInstructor}.
     *
     * @param desc        The description of the course.
     * @param instructors The list of instructors for the course.
     * @return The course description.
     */
    private String createCourseDescription(final String desc, final List<VisibleInstructor> instructors) {
        if (instructors == null || instructors.isEmpty())
            return desc;

        final StringBuilder sb = new StringBuilder(desc);
        sb.append("\n\n");
        sb.append(instructors.size() > 1 ? "Instructors: " : "Instructor: ");
        for (VisibleInstructor instructor : instructors) {
            sb.append(instructor.getDisplayName());
            if (instructors.indexOf(instructor) != instructors.size() - 1)
                sb.append(", ");
        }

        return sb.toString();
    }
}
