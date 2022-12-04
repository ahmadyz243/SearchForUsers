package com.happy_online.online_course.controllers;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.request.*;
import com.happy_online.online_course.payload.response.*;
import com.happy_online.online_course.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class TeacherController {
    final CourseService courseService;
    final TeacherService teacherService;
    final ExamService examService;
    final QuestionService questionService;
    final ExamQuestionService examQuestionService;
    final StudentService studentService;


    public TeacherController(CourseService courseService, TeacherService teacherService, ExamService examService, QuestionService questionService, ExamQuestionService examQuestionService, StudentService studentService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.examService = examService;
        this.questionService = questionService;
        this.examQuestionService = examQuestionService;
        this.studentService = studentService;
    }

    @GetMapping("/find/courses")
    public ResponseEntity<List<CourseInfoResponseTeacher>> findOwnCourse() {
        String currentTeacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(currentTeacherUsername);
        List<CourseInfoResponseTeacher> courses = courseService.mapCourseToResponse(teacher);
        return new ResponseEntity<>(courses, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/exam/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewExam(@RequestBody ExamCreateRequest examCreateRequest) {
        examService.saveExam(examCreateRequest);
        return ResponseEntity.ok("exam added");
    }

    @DeleteMapping("/exam/remove/{exam_id}")
    public ResponseEntity<?> removeExam(@PathVariable Long exam_id) {
        Exam exam = examService.findById(exam_id);
        examService.delete(exam);
        return ResponseEntity.ok("deleted");
    }

    //this is a feature for update an exam
//    @GetMapping("/exam/find-for-update/{exam_id}")
//    public ResponseEntity<ExamResponseForUpdate> formFillerUpdateExam(@PathVariable Long exam_id) {
//        ExamResponseForUpdate response = examService.findByIdResponse(exam_id);
//        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
//    }

    //it will get the required fields from 'formFillerUpdateExam' method and do the update
    @PutMapping("/exam/update/{exam_id}")
    public ResponseEntity<ExamResponseForUpdate> updateExam(@PathVariable Long exam_id, @RequestBody ExamUpdateRequest updateRequest) {
        Exam exam = examService.findById(exam_id);
        BeanUtils.copyProperties(updateRequest, exam);
        exam.setEndDate(exam.getStartDateAndTime().plusMinutes(updateRequest.getTime()));
        Exam updated = examService.save(exam);
        ExamResponseForUpdate examResponseForUpdate = new ExamResponseForUpdate();
        BeanUtils.copyProperties(updated, examResponseForUpdate);
        return new ResponseEntity<>(examResponseForUpdate, HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/find")
    public ResponseEntity<List<TeacherCourseResponse>> findTeacherCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(username);
        List<Course> courses = teacher.getCourseList();
        List<TeacherCourseResponse> responseTeachers = new ArrayList<>();
        courses.forEach(course -> {
            List<ExamResponseForUpdate> examResponse = examService.findByCourse(course);
            TeacherCourseResponse courseInfoResponse = new TeacherCourseResponse();
            BeanUtils.copyProperties(course, courseInfoResponse);
            courseInfoResponse.setId(course.getId());
            courseInfoResponse.setExam(examResponse);
            courseInfoResponse.setEnabled(course.getActive());
            responseTeachers.add(courseInfoResponse);
        });
        return new ResponseEntity<>(responseTeachers, HttpStatus.ACCEPTED);
    }

    //at first , we have to find questions in questions bank
    @GetMapping("/course/exam/find-questions/{courseId}")
    public ResponseEntity<List<QuestionResponse>> getCreatedQuestions(@PathVariable Long courseId) {
        String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<QuestionResponse> questions = questionService.getCompleteQuestions(teacherUsername, courseId);
        return new ResponseEntity<>(questions, HttpStatus.ACCEPTED);
    }


    //add question to exam from question bank
    @PutMapping(value = "/course/exam/add-question", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addQuestion(@RequestBody ExamQuestionInfo questionInfo) {
        examService.addQuestion(questionInfo);
        return ResponseEntity.ok("question added");
    }

    // add multiple choice question
    @PostMapping("/course/exam/add-multiple")
    public ResponseEntity<?> addMultipleChoiceQuestion(@RequestBody MultipleChoiceQuestionDTO multipleChoiceQuestion) {
        examService.addMultipleChoiceQuestion(multipleChoiceQuestion);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // add detailed question
    @PostMapping("/course/exam/add-detailed-exam")
    public ResponseEntity<?> addDetailedQuestion(@RequestBody DetailedQuestionDTO detailedQuestion) {
        examService.addDetailedQuestion(detailedQuestion);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //get the questions of an exam for edit or...
    @GetMapping("/course/exam/get/{exam_id}")
    public ResponseEntity<ExamResponseForView> getExamQuestions(@PathVariable Long exam_id) {
        Exam exam = examService.findById(exam_id);
        ExamResponseForView examResponse = examService.mapExamToExamResponseForView(exam);
        return new ResponseEntity<>(examResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/exam/find-by-id/{id}")
    public ResponseEntity<ExamQuestionResponse> findQuestionById(@PathVariable Long id) {
        String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        ExamQuestionResponse examResponse = examQuestionService.findByIdAndTeacher(id, teacherUsername);
        return new ResponseEntity<>(examResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/course/exam/auto-set-grade/{exam_id}/{course_id}")
    public ResponseEntity<?> autoSetGrade(@PathVariable Long exam_id, @PathVariable Long course_id) {
        examService.autoSetGrade(exam_id, course_id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/exam/get-students-answers/{exam_id}")
    public ResponseEntity<List<ExamStudentsResponse>> studentAnswers(@PathVariable Long exam_id) {
        List<ExamStudentsResponse> examStudentsResponses = studentService.findAllStudentsWithAnswers(exam_id);
        return new ResponseEntity<>(examStudentsResponses, HttpStatus.ACCEPTED);
    }

    @PutMapping("/course/exam/set-grade/{examQuestionAnswerId}/{grade}")
    public ResponseEntity<?> setGrade(@PathVariable Long examQuestionAnswerId, @PathVariable Double grade) {
        examQuestionService.setGrade(examQuestionAnswerId, grade);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/course/exam/show-grades/{exam_id}")
    public ResponseEntity<?> showGradesToStudents(@PathVariable Long exam_id) {
        examService.showGradesToStudents(exam_id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

