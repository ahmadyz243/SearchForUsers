package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamQuestionStudentAnswersResponse {
    private Long questionAnswerId;
    private String Answer;
    private Double earnedScore = 0D;
    private ExamQuestionResponse examQuestion;
}
