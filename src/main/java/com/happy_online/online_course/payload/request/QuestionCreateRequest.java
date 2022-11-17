package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCreateRequest {
    private Long teacherId;
    private Long courseId;
    private Long examId;
    private String title;
    private String question;
    private Double score;
}
