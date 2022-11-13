package com.happy_online.online_course.service;

import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.service.base.BaseService;

public interface ExamService extends BaseService<Exam, Long> {
    Exam saveExam(ExamCreateRequest examCreateRequest);
}
