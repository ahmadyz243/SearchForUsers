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
import java.util.Optional;

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
            Optional<StudentAnswers> answers = repository.findByStudentAndExam(student, exam);
            answers = answers.or(() -> {
                StudentAnswers studentAnswers1 = new StudentAnswers();
                studentAnswers1.setStudent(student);
                studentAnswers1.setExam(exam);
                return Optional.of(studentAnswers1);
            });
            List<Integer> integers = new ArrayList<>();
            answers.get().getExamQuestionAnswerList().forEach(answer -> {
                if (answer.getExamQuestion().getId().equals(answerRequest.getExamQuestionId())) {
                    answer.setAnswer(answerRequest.getAnswer());
                    integers.add(1);
                }
            });

            if (integers.size() == 0) {
                ExamQuestionAnswer examQuestionAnswer = new ExamQuestionAnswer();
                examQuestionAnswer.setAnswer(answerRequest.getAnswer());
                ExamQuestion examQuestion = examQuestionService.findById(answerRequest.getExamQuestionId());
                examQuestionAnswer.setExamQuestion(examQuestion);
                examQuestionAnswer.setStudentAnswers(answers.get());
                answers.get().setExamQuestionAnswerList(examQuestionAnswer);
            }
            repository.save(answers.get());
        } else {
            Optional<StudentAnswers> answer = repository.findByStudentAndExam(student, exam);
            answer.ifPresent(studentAnswers -> studentAnswers.setFinalized(true));
        }
    }

    @Override
    public List<ExamQuestionAnswerResponse> findStudentAnswers(Long exam_id) {
        List<ExamQuestionAnswerResponse> examQuestionAnswerResponses = new ArrayList<>();
        String studentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentService.findByUsername(studentUsername);
        Exam exam = examService.findById(exam_id);
        Optional<StudentAnswers> studentAnswers = repository.findByStudentAndExam(student, exam);
        if (studentAnswers.isPresent()) {
            studentAnswers.get().getExamQuestionAnswerList().forEach(answer -> {
                ExamQuestionAnswerResponse examQuestionAnswerResponse = new ExamQuestionAnswerResponse();
                examQuestionAnswerResponse.setAnswer(answer.getAnswer());
                examQuestionAnswerResponse.setExamQuestionId(answer.getExamQuestion().getQuestion().getId());
                examQuestionAnswerResponses.add(examQuestionAnswerResponse);
            });
            return examQuestionAnswerResponses;
        }
        return examQuestionAnswerResponses;
    }
}
