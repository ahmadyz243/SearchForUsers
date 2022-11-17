package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.mapper.QuestionMapper;
import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.DetailedQuestionDTO;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.response.QuestionResponse;
import com.happy_online.online_course.repository.QuestionRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.QuestionService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question, Long, QuestionRepository> implements QuestionService {

    public QuestionServiceImpl(QuestionRepository repository, TeacherService teacherService, CourseService courseService, QuestionMapper questionMapper) {
        super(repository);
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.questionMapper = questionMapper;
    }

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final QuestionMapper questionMapper;

    @Override

    public List<QuestionResponse> getCompleteQuestions(String teacherUsername, Long courseId) {
        Teacher teacher = teacherService.findByUsername(teacherUsername);
        Course course = courseService.findById(courseId);
        List<Question> questions = repository.findByTeacherAndCourse(teacher, course);
        return questionMapper.questionToQuestionResponseList(questions);
    }

    @Override
    @Transactional
    public Question save(MultipleChoiceQuestionDTO question) {
        String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(teacherUsername);
        Course course = courseService.findById(question.getCourseId());
        MultipleChoiceQuestion multipleChoiceQuestion = questionMapper.multipleChoiceQuestionDTOtoMultipleChoiceQuestion(question);
        multipleChoiceQuestion.setCourse(course);
        multipleChoiceQuestion.setTeacher(teacher);
        multipleChoiceQuestion.getQuestionItemList().forEach(item -> {
            item.setQuestion(multipleChoiceQuestion);
        });
        return repository.save(multipleChoiceQuestion);
    }

    @Override
    public Question save(DetailedQuestionDTO question) {
        String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(teacherUsername);
        Course course = courseService.findById(question.getCourseId());
        DetailedQuestion detailedQuestion = questionMapper.detailedQuestionDTOtoDetailedQuestion(question);
        detailedQuestion.setCourse(course);
        detailedQuestion.setTeacher(teacher);
        return repository.save(detailedQuestion);
    }

}
