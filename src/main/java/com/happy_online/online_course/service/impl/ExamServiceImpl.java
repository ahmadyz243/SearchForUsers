package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.response.ExamResponseForUpdate;
import com.happy_online.online_course.repository.ExamRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.QuestionService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpl extends BaseServiceImpl<Exam, Long, ExamRepository> implements ExamService {
    public ExamServiceImpl(ExamRepository repository, CourseService courseService, ExamRepository examRepository, QuestionService questionService) {
        super(repository);
        this.courseService = courseService;
        this.examRepository = examRepository;
        this.questionService = questionService;
    }

    final CourseService courseService;
    final ExamRepository examRepository;
    final QuestionService questionService;

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
    public void addQuestion(Long examId, Question question) {
        Exam exam = findById(examId);
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
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
        addQuestion(multipleChoiceQuestion.getExamId(), question);

    }


    private ExamResponseForUpdate mapExamToResponse(Exam exam) {
        ExamResponseForUpdate response = new ExamResponseForUpdate();
        BeanUtils.copyProperties(exam, response);
        response.setId(exam.getId());
        return response;
    }

    private Exam mapCreateReqToExam(ExamCreateRequest examCreateRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateRequest, exam);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
//        String strDate = formatter.format(examCreateRequest.getTime());
//        exam.setTime(LocalDateTime.parse(strDate,formatter));
        return exam;
    }
}
