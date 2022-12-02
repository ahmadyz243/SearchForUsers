package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.ExamQuestionMapper;
import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.StudentAnswerRequest;
import com.happy_online.online_course.payload.response.ExamQuestionAnswerResponse;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
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
                StudentGrade studentGrade = new StudentGrade();
                StudentAnswers studentAnswers1 = new StudentAnswers();
                studentAnswers1.setStudent(student);
                studentAnswers1.setExam(exam);
                studentGrade.setStudent(student);
                studentGrade.setExam(exam);
                studentAnswers1.setGrade(studentGrade);
                studentGrade.setStudentAnswers(studentAnswers1);
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

    @Override
    @Transactional
    public void addAllAnswers(List<StudentAnswerRequest> studentAnswerRequests, Long exam_id) {
        Student student = studentService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Exam exam = examService.findById(exam_id);
        Optional<StudentAnswers> studentAnswers = repository.findByStudentAndExam(student, exam);

        studentAnswers = studentAnswers.or(() -> {
            StudentGrade studentGrade = new StudentGrade();
            StudentAnswers studentAnswers1 = new StudentAnswers();
            studentAnswers1.setStudent(student);
            studentAnswers1.setExam(exam);
            studentGrade.setStudent(student);
            studentGrade.setExam(exam);
            studentAnswers1.setGrade(studentGrade);
            studentGrade.setStudentAnswers(studentAnswers1);
            return Optional.of(studentAnswers1);
        });

        //all questions for check the answers
        List<ExamQuestion> examQuestions = exam.getExamQuestionList();

        //These are all the answers came from UI
        List<ExamQuestionAnswer> examQuestionAnswerList = new ArrayList<>();

        //this is the last answers of student
        List<ExamQuestionAnswer> studentExamQuestionAnswers = studentAnswers.get().getExamQuestionAnswerList();

        Optional<StudentAnswers> finalStudentAnswers = studentAnswers;
        examQuestions.forEach(examQuestion -> {
            studentAnswerRequests.forEach(studentAnswer -> {
                if (studentAnswer.getExamQuestionId().equals(examQuestion.getId())) {
                    ExamQuestionAnswer examQuestionAnswer = new ExamQuestionAnswer();
                    examQuestionAnswer.setStudentAnswers(finalStudentAnswers.get());
                    examQuestionAnswer.setExamQuestion(examQuestion);
                    examQuestionAnswer.setAnswer(studentAnswer.getAnswer());
                    examQuestionAnswerList.add(examQuestionAnswer);
                }
            });
        });
        studentExamQuestionAnswers.forEach(allStudentAnswers -> {
            examQuestionAnswerList.forEach(uiAnswers -> {
                if (allStudentAnswers.getExamQuestion().equals(uiAnswers.getExamQuestion())) {
                    allStudentAnswers.setAnswer(uiAnswers.getAnswer());
                }
            });
        });
        studentAnswers.get().setExamQuestionAnswerList(studentExamQuestionAnswers);
        studentAnswers.get().setFinalized(true);
        repository.save(studentAnswers.get());
    }

    @Override
    @Transactional
    public Boolean checkForEnd(Long exam_id, String username) {
        Student student = studentService.findByUsername(username);
        Exam exam = examService.findById(exam_id);
        Optional<StudentAnswers> studentAnswers = repository.findByStudentAndExam(student, exam);

        studentAnswers = studentAnswers.or(() -> {
            StudentGrade studentGrade = new StudentGrade();
            StudentAnswers studentAnswers1 = new StudentAnswers();
            studentAnswers1.setStudent(student);
            studentAnswers1.setExam(exam);
            studentGrade.setStudent(student);
            studentGrade.setExam(exam);
            studentAnswers1.setGrade(studentGrade);
            studentGrade.setStudentAnswers(studentAnswers1);
            return Optional.of(studentAnswers1);
        });
        if (studentAnswers.isPresent()) {
            return studentAnswers.get().getFinalized();
        }
        return true;
    }
}
