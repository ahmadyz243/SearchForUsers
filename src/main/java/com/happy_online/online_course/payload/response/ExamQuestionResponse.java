package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamQuestionResponse {
    private Double score;
    private QuestionResponse question;
}
