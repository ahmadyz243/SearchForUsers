package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.repository.ExamRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExamServiceImpl extends BaseServiceImpl<Exam, Long, ExamRepository> implements ExamService {
    public ExamServiceImpl(ExamRepository repository, CourseService courseService, ExamRepository examRepository) {
        super(repository);
        this.courseService = courseService;
        this.examRepository = examRepository;
    }

    final CourseService courseService;
    final ExamRepository examRepository;


    @Override
    public Exam saveExam(ExamCreateRequest examCreateRequest) {
        Course course = courseService.findById(examCreateRequest.getCourseId());
        Exam exam = mapCreateReqToExam(examCreateRequest);
        exam.setCourse(course);
        return repository.save(exam);
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
