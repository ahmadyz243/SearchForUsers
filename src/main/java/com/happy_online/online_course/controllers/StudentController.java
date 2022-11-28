package com.happy_online.online_course.controllers;

import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.payload.response.ExamResponseForView;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamQuestionService;
import com.happy_online.online_course.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final CourseService courseService;
    private final ExamService examService;
    private final ExamQuestionService examQuestionService;

    public StudentController(CourseService courseService, ExamService examService, ExamQuestionService examQuestionService) {
        this.courseService = courseService;
        this.examService = examService;
        this.examQuestionService = examQuestionService;
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseInfoResponseForStudent>> getStudentExams() {
        String studentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CourseInfoResponseForStudent> studentCourseList = courseService.findStudentCourses(studentUsername);
        return new ResponseEntity<>(studentCourseList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/exam/get/{exam_id}")
    public ResponseEntity<ExamResponseForView> startExam(@PathVariable Long exam_id) {
        String studentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        ExamResponseForView examDetails = examService.findByIdForStart(exam_id, studentUsername);
        return new ResponseEntity<>(examDetails, HttpStatus.ACCEPTED);
    }
    @GetMapping("/course/exam/get-questions/{exam_id}")
    public ResponseEntity<List<ExamQuestionResponse>> getQuestionForStart(@PathVariable Long exam_id) {
        List<ExamQuestionResponse> examQuestionResponses = examQuestionService.findAllByExamId(exam_id);
        return new ResponseEntity<>(examQuestionResponses, HttpStatus.ACCEPTED);
    }

}
