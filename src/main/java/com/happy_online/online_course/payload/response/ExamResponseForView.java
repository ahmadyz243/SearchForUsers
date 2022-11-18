package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExamResponseForView {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime startDateAndTime;

    private Integer time;

    private List<ExamQuestionResponse> examQuestionList;
}
