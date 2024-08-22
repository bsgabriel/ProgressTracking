package com.progress.tracking.rest.mapper;

import com.progress.tracking.rest.dto.ChapterDTO;
import com.progress.tracking.rest.dto.CourseDTO;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.pojo.Result;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for mapping courses from different platforms to the {@linkplain CourseDTO} entity.
 */
@Service
public class CourseMapper {

    /**
     * Maps a {@linkplain UdemyCourse} object to a {@linkplain CourseDTO} object, including the chapters and lessons from the Udemy course curriculum.
     *
     * @param udemyCourse The {@linkplain UdemyCourse} object to map.
     * @param chapters    The {@linkplain Map} object containing the chapters and lessons for the Udemy course.
     * @return A Course object mapped from the {@linkplain UdemyCourse}.
     */
    public CourseDTO courseFromUdemy(final UdemyCourse udemyCourse, final Map<Result, List<Result>> chapters) {
        if (udemyCourse == null)
            return null;

        return CourseDTO.builder()
                .id(udemyCourse.getId())
                .name(udemyCourse.getTitle())
                .desc(createCourseDescription(udemyCourse.getHeadline(), udemyCourse.getInstructors()))
                .image(udemyCourse.getImage())
                .url(udemyCourse.getUrl())
                .chapters(createChapetrsList(chapters))
                .build();
    }

    /**
     * Converts the {@linkplain Map} into a list of chapters for the {@linkplain CourseDTO}.
     * Each chapter contains its name and a list of lessons.
     *
     * @param uChapters A map representing Udemy course chapters and their corresponding lectures.
     * @return A list of {@linkplain ChapterDTO} objects representing the chapters of the course.
     */
    private List<ChapterDTO> createChapetrsList(Map<Result, List<Result>> uChapters) {
        var chapters = new ArrayList<ChapterDTO>();
        if (uChapters == null || uChapters.isEmpty())
            return chapters;

        int idxItem = 1;
        for (Result uChapter : uChapters.keySet()) {
            var lectures = uChapters.get(uChapter);
            if (lectures == null || lectures.isEmpty())
                continue;

            var lessons = new ArrayList<String>();
            for (Result lecture : lectures) {
                lessons.add(idxItem + ". " + lecture.getTitle());
                idxItem++;
            }

            chapters.add(ChapterDTO.builder()
                    .name(uChapter.getTitle())
                    .lessons(lessons)
                    .build());
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
