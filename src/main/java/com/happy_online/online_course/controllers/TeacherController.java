package com.happy_online.online_course.controllers;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.request.ExamCreateRequest;
import com.happy_online.online_course.payload.request.ExamUpdateRequest;
import com.happy_online.online_course.payload.response.*;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.TeacherService;
import org.apache.coyote.Response;
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

    public TeacherController(CourseService courseService, TeacherService teacherService, ExamService examService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.examService = examService;
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
    @GetMapping("/exam/find-for-update/{exam_id}")
    public ResponseEntity<ExamResponseForUpdate> formFillerUpdateExam(@PathVariable Long exam_id) {
        ExamResponseForUpdate response = examService.findByIdResponse(exam_id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    //it will get the required fields from 'formFillerUpdateExam' method and do the update
    @PutMapping("/exam/update/{exam_id}")
    public ResponseEntity<ExamResponseForUpdate> updateExam(@PathVariable Long exam_id, @RequestBody ExamUpdateRequest updateRequest) {
        Exam exam = examService.findById(exam_id);
        BeanUtils.copyProperties(updateRequest, exam);
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

    //add question to exam
    @PutMapping(value = "course/exam/add-question", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addQuestion() {

        return ResponseEntity.ok("question added");
    }

    //at the first we have to find questions in bank
    @GetMapping("/course/exam/find-questions/{courseId}")
    public ResponseEntity<List<QuestionResponse>> getCreatedQuestions(@PathVariable Long courseId) {
        String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(teacherUsername);
        return null;
    }
}
