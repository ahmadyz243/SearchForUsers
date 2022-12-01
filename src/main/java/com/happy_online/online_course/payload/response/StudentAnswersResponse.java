package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.ExamQuestionAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class StudentAnswersResponse {
    private List<ExamQuestionAnswerResponse> examQuestionAnswerList;
}
