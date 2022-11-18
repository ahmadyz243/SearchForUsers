package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.ExamQuestion;

import com.happy_online.online_course.repository.ExamQuestionRepository;
import com.happy_online.online_course.service.ExamQuestionService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ExamQuestionServiceImpl extends BaseServiceImpl<ExamQuestion, Long, ExamQuestionRepository> implements ExamQuestionService {
    public ExamQuestionServiceImpl(ExamQuestionRepository repository) {
        super(repository);
    }

}
