package com.progress.tracking.rest.mapper;

import com.progress.tracking.rest.entity.Course;
import com.progress.tracking.wrapper.udemy.entity.UdemyCourse;
import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for mapping courses from different platforms to the {@linkplain Course} entity.
 */
@Service
public class CourseMapper {

    /**
     * Maps {@linkplain UdemyCourse} object to a {@linkplain Course} object.
     *
     * @param udemyCourse The {@linkplain UdemyCourse} object to map.
     * @return A Course object mapped from the {@linkplain UdemyCourse}.
     */
    public Course udemyCourseToCourse(final UdemyCourse udemyCourse) {
        if (udemyCourse == null)
            return null;

        final Course course = new Course();
        course.setId(udemyCourse.getId());
        course.setName(udemyCourse.getTitle());
        course.setDesc(createCourseDescription(udemyCourse.getHeadline(), udemyCourse.getInstructors()));
        course.setImage(udemyCourse.getImage());
        course.setUrl(udemyCourse.getUrl());
        return course;
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
