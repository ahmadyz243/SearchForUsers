package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentAnswerRequest {
    private String answer;
    private Long examQuestionId;
    private Long exam_id;
}
