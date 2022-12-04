package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.ExamQuestionMapper;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.models.ExamQuestionAnswer;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.repository.ExamQuestionRepository;
import com.happy_online.online_course.service.ExamQuestionAnswerService;
import com.happy_online.online_course.service.ExamQuestionService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamQuestionServiceImpl extends BaseServiceImpl<ExamQuestion, Long, ExamQuestionRepository> implements ExamQuestionService {
    public ExamQuestionServiceImpl(ExamQuestionRepository repository, ExamQuestionMapper examQuestionMapper, ExamService examService, ExamQuestionAnswerService examQuestionAnswerService) {
        super(repository);
        this.examQuestionMapper = examQuestionMapper;
        this.examService = examService;
        this.examQuestionAnswerService = examQuestionAnswerService;
    }

    private final ExamQuestionMapper examQuestionMapper;
    private final ExamService examService;
    private final ExamQuestionAnswerService examQuestionAnswerService;

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
            ExamQuestionResponse examQuestionResponse = examQuestionMapper.examQuestionToExamQuestionResponse(examQuestion);
            examQuestionResponse.setExamQuestionId(examQuestion.getId());
            examQuestionResponses.add(examQuestionResponse);

        });
        return examQuestionResponses;
    }

    @Override
    @Transactional
    public void setGrade(Long examQuestionAnswerId, Double grade) {
        ExamQuestionAnswer examQuestionAnswer = examQuestionAnswerService.findById(examQuestionAnswerId);
        if (grade <= examQuestionAnswer.getExamQuestion().getScore()) {
            Double currentScore = examQuestionAnswer.getEarnedScore();
            Double fullScore = examQuestionAnswer.getStudentAnswers().getGrade().getScore();
            fullScore = fullScore - currentScore;
            examQuestionAnswer.setEarnedScore(grade);
            fullScore = fullScore + grade;
            System.out.println(currentScore);
            System.out.println(grade);
            System.out.println(fullScore);
            examQuestionAnswer.getStudentAnswers().getGrade().fullSetScore(fullScore);
        } else {
            throw new BadCredentialsException("grade is too big");
        }

    }
}
