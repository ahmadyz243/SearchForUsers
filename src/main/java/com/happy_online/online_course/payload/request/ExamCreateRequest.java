package com.happy_online.online_course.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Getter
public class ExamCreateRequest {

    private LocalDateTime time;
    private LocalDateTime startDateAndTime;
    private String description;
    private String title;
    private Long courseId;

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

//    public void setTime(LocalDateTime time) {
//        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
//        this.time = LocalDateTime.parse(timeFormatter.format(time));
//    }

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
