package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamResponseForStudent {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDateAndTime;
    private LocalDateTime endDate;
    private Integer time;
    private Boolean enabled;
    private Double score = 0D;
}
