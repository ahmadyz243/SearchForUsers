package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamStudentsResponse {
    private StudentResponse student;
    private LastStudentsAnswersResponse StudentAnswers;
    private List<ExamQuestionResponse> examQuestionResponses;
}
