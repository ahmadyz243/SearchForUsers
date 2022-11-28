package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.ExamQuestionMapper;
import com.happy_online.online_course.mapper.QuestionMapper;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;

import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.repository.ExamQuestionRepository;
import com.happy_online.online_course.service.ExamQuestionService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamQuestionServiceImpl extends BaseServiceImpl<ExamQuestion, Long, ExamQuestionRepository> implements ExamQuestionService {
    public ExamQuestionServiceImpl(ExamQuestionRepository repository, ExamQuestionMapper examQuestionMapper, ExamService examService) {
        super(repository);
        this.examQuestionMapper = examQuestionMapper;
        this.examService = examService;
    }

    private final ExamQuestionMapper examQuestionMapper;
    private final ExamService examService;

    @Override
    public ExamQuestionResponse findByIdAndTeacher(Long id, String teacherUsername) {
        ExamQuestion examQuestion = findById(id);
        if (examQuestion.getQuestion().getTeacher().getUsername().equals(teacherUsername)) {
            ExamQuestionResponse questionResponse = examQuestionMapper.examQuestionToExamQuestionResponse(examQuestion);
            return questionResponse;
        } else {
            throw new BadCredentialsException("you cant update this question");
        }
    }

    @Override
    public List<ExamQuestionResponse> findAllByExamId(Long exam_id) {
        Exam exam = examService.findById(exam_id);
        List<ExamQuestion> examQuestions = repository.findByExam(exam);
        List<ExamQuestionResponse> examQuestionResponses = new ArrayList<>();
        examQuestions.forEach(examQuestion -> {
            examQuestionResponses.add(examQuestionMapper.examQuestionToExamQuestionResponse(examQuestion));
        });
        return examQuestionResponses;
    }
}
