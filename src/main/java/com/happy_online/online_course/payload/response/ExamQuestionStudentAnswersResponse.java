package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.ExamQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamQuestionStudentAnswersResponse {
    private String Answer;
    private ExamQuestionResponse examQuestion;
}
