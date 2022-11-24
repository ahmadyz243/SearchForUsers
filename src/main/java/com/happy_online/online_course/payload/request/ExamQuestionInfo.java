package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamQuestionInfo {
    private Long questionId;
    private Long examId;
    private Double score;
}
