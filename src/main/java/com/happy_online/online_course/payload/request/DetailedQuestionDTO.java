package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailedQuestionDTO {

    private Long examId;

    private Double score;

    private Long courseId;

    private String title;

    private String question;
}
