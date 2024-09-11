package com.progress.tracking.wrapper.udemy.entity;

import com.progress.tracking.wrapper.udemy.pojo.VisibleInstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UdemyCourse {
    private Integer id;
    private String title;
    private String headline;
    private List<VisibleInstructor> instructors;
    private String url;
    private String image;

    @Override
    public String toString() {
        return "UdemySearchedCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headline='" + headline + '\'' +
                ", instructors=" + instructors +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
