package com.happy_online.online_course.service;

import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface ExamQuestionService extends BaseService<ExamQuestion, Long> {
    ExamQuestionResponse findByIdAndTeacher(Long id, String teacherUsername);

    List<ExamQuestionResponse> findAllByExamId(Long exam_id);
}
