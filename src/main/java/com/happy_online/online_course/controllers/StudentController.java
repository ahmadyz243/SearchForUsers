package com.happy_online.online_course.controllers;

import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.request.StudentAnswerRequest;
import com.happy_online.online_course.payload.response.ExamQuestionAnswerResponse;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.payload.response.ExamResponseForView;
import com.happy_online.online_course.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final ExamService examService;
    private final ExamQuestionService examQuestionService;
    private final StudentAnswersService studentAnswersService;

    public StudentController(StudentService studentService, CourseService courseService, ExamService examService, ExamQuestionService examQuestionService, StudentAnswersService studentAnswersService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.examService = examService;
        this.examQuestionService = examQuestionService;
        this.studentAnswersService = studentAnswersService;
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

    @GetMapping("/course/exam/get-student-answers/{exam_id}")
    public ResponseEntity<List<ExamQuestionAnswerResponse>> getStudentAnswer(@PathVariable Long exam_id) {
        List<ExamQuestionAnswerResponse> examQuestionAnswerResponses = studentAnswersService.findStudentAnswers(exam_id);
        return new ResponseEntity<>(examQuestionAnswerResponses, HttpStatus.ACCEPTED);
    }

    @PostMapping("/course/exam/set-answer")
    public ResponseEntity<?> setAnswer(@RequestBody StudentAnswerRequest answerRequest) {
        studentAnswersService.addAnswer(answerRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/course/exam/finish/{exam_id}")
    public ResponseEntity<?> finishExam(@PathVariable Long exam_id, @RequestBody List<StudentAnswerRequest> studentAnswerRequests) {
        studentAnswersService.addAllAnswers(studentAnswerRequests, exam_id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/exam/check-for-end/{exam_id}")
    public ResponseEntity<Boolean> checkForEnd(@PathVariable Long exam_id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Boolean check = studentAnswersService.checkForEnd(exam_id, username);
        return new ResponseEntity<>(check, HttpStatus.ACCEPTED);
    }


}
