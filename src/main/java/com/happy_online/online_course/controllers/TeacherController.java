package com.happy_online.online_course.controllers;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.response.CourseInfoResponse;
import com.happy_online.online_course.payload.response.CourseInfoResponseTeacher;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class TeacherController {
    final CourseService courseService;
    final TeacherService teacherService;

    public TeacherController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping("/find/courses")
    public ResponseEntity<List<CourseInfoResponseTeacher>> findOwnCourse() {
        String currentTeacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByUsername(currentTeacherUsername);
        List<CourseInfoResponseTeacher> courses = courseService.mapCourseToResponse(teacher);
        return new ResponseEntity<>(courses, HttpStatus.ACCEPTED);
    }
}
