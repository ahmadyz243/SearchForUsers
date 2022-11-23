package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamResponseForStudent {
    private String title;
    private String description;
    private LocalDateTime startDateAndTime;
    private Integer time;
}
