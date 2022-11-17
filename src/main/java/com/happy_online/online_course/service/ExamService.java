package com.happy_online.online_course.service;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.response.ExamResponseForUpdate;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface ExamService extends BaseService<Exam, Long> {
    Exam saveExam(ExamCreateRequest examCreateRequest);

    ExamResponseForUpdate findByIdResponse(Long exam_id);

    Exam save(Exam exam);

    List<ExamResponseForUpdate> findByCourse(Course course);

    void addQuestion(Long examId, Question question);

    void addQuestion(Long examId, Long questionId);


    void addMultipleChoiceQuestion(MultipleChoiceQuestionDTO multipleChoiceQuestion);
}
