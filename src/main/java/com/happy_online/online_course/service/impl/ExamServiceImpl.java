package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.ExamMapper;
import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public void addQuestion(Long examId, Long questionId) {
        Exam exam = findById(examId);
        ExamQuestion examQuestion = new ExamQuestion();
        Question question = questionService.findById(questionId);
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
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
        exam.getStudentsAnswers().forEach(studentsAnswers -> {
            if (studentsAnswers.getStudent().getUsername().equals(studentUsername)) {
                throw new BadCredentialsException("bro you cant join this exam again!");
            }
        });
        return mapExamToExamResponseForView(exam);
    }

    private Exam mapCreateReqToExam(ExamCreateRequest examCreateRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateRequest, exam);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return exam;
    }
}
