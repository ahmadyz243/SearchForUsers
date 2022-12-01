package com.happy_online.online_course.service;

import com.happy_online.online_course.models.StudentAnswers;
import com.happy_online.online_course.payload.request.StudentAnswerRequest;
import com.happy_online.online_course.payload.response.ExamQuestionAnswerResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface StudentAnswersService extends BaseService<StudentAnswers, Long> {
    void addAnswer(StudentAnswerRequest answerRequest);

    List<ExamQuestionAnswerResponse> findStudentAnswers(Long exam_id);
}
