package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.ExamMapper;
import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.DetailedQuestionDTO;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.payload.request.ExamQuestionInfo;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.response.ExamResponseForUpdate;
import com.happy_online.online_course.payload.response.ExamResponseForView;
import com.happy_online.online_course.repository.ExamRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.QuestionService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Inheritance;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExamServiceImpl extends BaseServiceImpl<Exam, Long, ExamRepository> implements ExamService {
    public ExamServiceImpl(ExamRepository repository, CourseService courseService, ExamRepository examRepository, QuestionService questionService, ExamMapper examMapper) {
        super(repository);
        this.courseService = courseService;
        this.examRepository = examRepository;
        this.questionService = questionService;
        this.examMapper = examMapper;
    }

    final CourseService courseService;
    final ExamRepository examRepository;
    final QuestionService questionService;
    final ExamMapper examMapper;

    @Override
    public Exam saveExam(ExamCreateRequest examCreateRequest) {
        Course course = courseService.findById(examCreateRequest.getCourseId());
        Exam exam = mapCreateReqToExam(examCreateRequest);

        exam.setCourse(course);
        exam.setEndDate(exam.getStartDateAndTime().plusMinutes(exam.getTime()));
        return repository.save(exam);
    }

    @Override
    public ExamResponseForUpdate findByIdResponse(Long exam_id) {
        Exam exam = findById(exam_id);
        return mapExamToResponse(exam);
    }

    @Override
    public Exam save(Exam exam) {
        return repository.save(exam);
    }

    @Override
    public List<ExamResponseForUpdate> findByCourse(Course course) {
        List<Exam> exam = repository.findByCourse(course);
        List<ExamResponseForUpdate> response = new ArrayList<>();
        exam.forEach(examFor -> {
            ExamResponseForUpdate examResponseForUpdate = new ExamResponseForUpdate();
            response.add(mapExamToResponse(examFor));
        });
        return response;
    }

    @Override
    @Transactional
    public void addQuestion(Long examId, Question question, Double score) {
        Exam exam = findById(examId);
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
        examQuestion.setScore(score);
        exam.setExamQuestionList(examQuestion);
    }

    @Override
    @Transactional
    public void addQuestion(ExamQuestionInfo questionInfo) {
        Exam exam = findById(questionInfo.getExamId());
        ExamQuestion examQuestion = new ExamQuestion();
        Question question = questionService.findById(questionInfo.getQuestionId());
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
        examQuestion.setScore(questionInfo.getScore());
        exam.setExamQuestionList(examQuestion);
    }

    @Override
    public void addMultipleChoiceQuestion(MultipleChoiceQuestionDTO multipleChoiceQuestion) {
        Question question = questionService.save(multipleChoiceQuestion);
        addQuestion(multipleChoiceQuestion.getExamId(), question, multipleChoiceQuestion.getScore());

    }


    private ExamResponseForUpdate mapExamToResponse(Exam exam) {
        ExamResponseForUpdate response = new ExamResponseForUpdate();
        BeanUtils.copyProperties(exam, response);
        response.setId(exam.getId());
        return response;
    }

    @Override
    public ExamResponseForView mapExamToExamResponseForView(Exam exam) {
        return examMapper.examsToExamResponseForViewList(exam);
    }

    @Override
    public ExamResponseForView findByIdForStart(Long exam_id, String studentUsername) {
        Exam exam = findById(exam_id);
        if (exam.getStartDateAndTime().isAfter(LocalDateTime.now()) || exam.getEndDate().isBefore(LocalDateTime.now())) {
            // TODO: change the exception
            throw new BadCredentialsException("exam is not start or time is up");
        }
        exam.getStudentAnswers().forEach(studentsAnswers -> {
            if (studentsAnswers.getStudent().getUsername().equals(studentUsername)) {
                throw new BadCredentialsException("bro you cant join this exam again!");
            }
        });
        return mapExamToExamResponseForView(exam);
    }

    @Override
    public void addDetailedQuestion(DetailedQuestionDTO detailedQuestion) {
        Question question = questionService.save(detailedQuestion);
        addQuestion(detailedQuestion.getExamId(), question, detailedQuestion.getScore());
    }

    @Override
    @Transactional
    public void autoSetGrade(Long exam_id, Long course_id) {
        Course course = courseService.findById(course_id);
        List<Exam> exams = repository.findByCourse(course);
        Exam exam = new Exam();
        for (int i = 0; i < course.getExamList().size(); i++) {
            if (exam_id.equals(course.getExamList().get(i).getId())) {
                exam = course.getExamList().get(i);
                break;
            }
        }
        if (exam.getEndDate().isAfter(LocalDateTime.now())) {
            List<Student> students = course.getStudentList();
            List<StudentAnswers> studentsAnswers = exam.getStudentAnswers();
            Exam finalExam = exam;
            students.forEach(student -> {
                studentsAnswers.forEach(studentAnswers -> {
                    if (student == studentAnswers.getStudent()) {
                        studentAnswers.getExamQuestionAnswerList().forEach(studentAnswer -> {
                            MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) studentAnswer.getExamQuestion().getQuestion();
                            if (multipleChoiceQuestion.getQuestionItemList() != null) {
                                multipleChoiceQuestion.getQuestionItemList().forEach(questionItem -> {
                                    if (questionItem.getIsRightAnswer()) {
                                        String correctAnswer = questionItem.getAnswer();
                                        if (studentAnswer.getAnswer().equals(correctAnswer)) {
                                            student.getStudentGrades().forEach(grade -> {
                                                if (grade.getExam() == finalExam) {
                                                    if (!grade.getIsAutoSet()) {
                                                        grade.setScore(studentAnswer.getExamQuestion().getScore());
                                                    }
                                                    grade.setIsAutoSet(true);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            });
        } else {
            throw new BadCredentialsException("you can just set the scores after the exam!");
        }

    }


    private Exam mapCreateReqToExam(ExamCreateRequest examCreateRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateRequest, exam);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return exam;
    }
}
