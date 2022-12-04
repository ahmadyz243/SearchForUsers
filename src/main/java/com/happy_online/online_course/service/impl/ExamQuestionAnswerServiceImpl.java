package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.ExamQuestionAnswer;
import com.happy_online.online_course.repository.ExamQuestionAnswerRepository;
import com.happy_online.online_course.service.ExamQuestionAnswerService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ExamQuestionAnswerServiceImpl extends BaseServiceImpl<ExamQuestionAnswer, Long, ExamQuestionAnswerRepository> implements ExamQuestionAnswerService {
    public ExamQuestionAnswerServiceImpl(ExamQuestionAnswerRepository repository) {
        super(repository);
    }
}
