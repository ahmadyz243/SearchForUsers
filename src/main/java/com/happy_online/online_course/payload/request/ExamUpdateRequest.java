package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamUpdateRequest {
    private LocalDateTime time;
    private LocalDateTime startDateAndTime;
    private String description;
    private String title;
}
