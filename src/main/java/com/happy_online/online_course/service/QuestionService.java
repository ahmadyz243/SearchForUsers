package com.happy_online.online_course.service;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.response.QuestionResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface QuestionService extends BaseService<Question, Long> {
    List<QuestionResponse> getCompleteQuestions(Teacher teacher, Course course);
}
