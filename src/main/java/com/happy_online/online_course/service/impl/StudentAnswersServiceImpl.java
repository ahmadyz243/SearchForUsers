package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.StudentAnswerRequest;
import com.happy_online.online_course.payload.response.ExamQuestionAnswerResponse;
import com.happy_online.online_course.repository.StudentAnswersRepository;
import com.happy_online.online_course.service.ExamQuestionService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.StudentAnswersService;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentAnswersServiceImpl extends BaseServiceImpl<StudentAnswers, Long, StudentAnswersRepository> implements StudentAnswersService {
    public StudentAnswersServiceImpl(StudentAnswersRepository repository, StudentService studentService, ExamService examService, ExamQuestionService examQuestionService) {
        super(repository);
        this.studentService = studentService;
        this.examService = examService;
        this.examQuestionService = examQuestionService;
    }

    private final StudentService studentService;
    private final ExamService examService;
    private final ExamQuestionService examQuestionService;

    @Override
    @Transactional
    public void addAnswer(StudentAnswerRequest answerRequest) {
        Exam exam = examService.findById(answerRequest.getExam_id());
        String studentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentService.findByUsername(studentUsername);
        if (LocalDateTime.now().isBefore(exam.getEndDate())) {

            StudentAnswers answers = repository.findByStudentAndExam(student, exam);
            if (answers == null) {
                answers = new StudentAnswers();
                answers.setStudent(student);
                answers.setExam(exam);
            }
            List<Integer> integers = new ArrayList<>();
            if (answers.getExamQuestionAnswerList() != null) {
                answers.getExamQuestionAnswerList().forEach(answer -> {
                    if (answer.getExamQuestion().getId() == answerRequest.getExamQuestionId()) {
                        answer.setAnswer(answerRequest.getAnswer());
                        integers.add(1);
                    }
                });
            }

            if (integers.size() == 0) {
                ExamQuestionAnswer examQuestionAnswer = new ExamQuestionAnswer();
                examQuestionAnswer.setAnswer(answerRequest.getAnswer());
                ExamQuestion examQuestion = examQuestionService.findById(answerRequest.getExamQuestionId());
                examQuestionAnswer.setExamQuestion(examQuestion);
                examQuestionAnswer.setStudentAnswers(answers);
                answers.setExamQuestionAnswerList(examQuestionAnswer);
            }
            repository.save(answers);
        } else {
            StudentAnswers answer = repository.findByStudentAndExam(student, exam);
            answer.setFinalized(true);
        }

    }

    @Override
    public List<ExamQuestionAnswerResponse> findStudentAnswers(Long exam_id) {
        String studentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentService.findByUsername(studentUsername);
        Exam exam = examService.findById(exam_id);
        StudentAnswers studentAnswers = repository.findByStudentAndExam(student, exam);
        List<ExamQuestionAnswerResponse> examQuestionAnswerResponses = new ArrayList<>();
        if (studentAnswers.getExamQuestionAnswerList() != null) {
            studentAnswers.getExamQuestionAnswerList().forEach(answer -> {
                ExamQuestionAnswerResponse examQuestionAnswerResponse = new ExamQuestionAnswerResponse();
                examQuestionAnswerResponse.setAnswer(answer.getAnswer());
                examQuestionAnswerResponse.setExamQuestionId(answer.getExamQuestion().getQuestion().getId());
                examQuestionAnswerResponses.add(examQuestionAnswerResponse);
            });
            return examQuestionAnswerResponses;
        }
        return null;
    }
}
