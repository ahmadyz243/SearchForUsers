package com.happy_online.online_course.payload.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExamCreateRequest {

    private Integer time;
    private LocalDateTime startDateAndTime;
    private String description;
    private String title;
    private Long courseId;

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setStartDateAndTime(LocalDateTime startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
