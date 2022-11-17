package com.happy_online.online_course.service;

import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.DetailedQuestionDTO;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.request.QuestionCreateRequest;
import com.happy_online.online_course.payload.response.QuestionResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface QuestionService extends BaseService<Question, Long> {
    List<QuestionResponse> getCompleteQuestions(String teacherUsername, Long courseId);

    Question save(MultipleChoiceQuestionDTO question);

    Question save(DetailedQuestionDTO question);
}
