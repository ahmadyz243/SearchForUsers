package com.happy_online.online_course.controllers;

import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.CreateCourseRequest;
import com.happy_online.online_course.payload.request.UserSearchRequest;
import com.happy_online.online_course.payload.request.UserUpdateRequest;
import com.happy_online.online_course.payload.response.CourseInfoResponse;
import com.happy_online.online_course.payload.response.MessageResponse;
import com.happy_online.online_course.payload.response.UserInfoResponse;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    final UserService userService;
    final CourseService courseService;

    public AdminController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/user/not-actives")
    public ResponseEntity<List<UserInfoResponse>> getNotEnabled() {
        List<UserInfoResponse> userInfoResponses = userService.notEnabledUsers(false);
        return ResponseEntity.ok(userInfoResponses);
    }

    @PutMapping("/user/active-by-id/{id}")
    public ResponseEntity<?> activeUserById(@PathVariable Long id) {
        userService.activeById(id);
        return ResponseEntity.ok(new MessageResponse("successfully activated"));
    }

    @PostMapping("/course/create")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest courseRequest) {
        courseService.saveCourse(courseRequest);
        return new ResponseEntity<>("course created ", HttpStatus.CREATED);
    }

    @PutMapping("/course/add-teacher/{course_id}/{teacher_id}")
    public ResponseEntity<?> addTeacherToCourse(@PathVariable Long course_id, @PathVariable Long teacher_id) {
        courseService.addTeacher(course_id, teacher_id);
        return new ResponseEntity<>("teacher added successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/course/remove-teacher/{course_id}")
    public ResponseEntity<?> removeTeacherFromCourse(@PathVariable Long course_id) {
        courseService.removeTeacher(course_id);
        return new ResponseEntity<>("course updated successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/course/add-student/{course_id}/{student_id}")
    public ResponseEntity<?> addStudentToCourse(@PathVariable Long course_id, @PathVariable Long student_id) {
        courseService.addStudent(course_id, student_id);
        return new ResponseEntity<>("student added successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/course/remove-student/{course_id}/{student_id}")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable Long course_id, @PathVariable Long student_id) {
        courseService.removeStudent(course_id, student_id);
        return new ResponseEntity<>("student removed successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/find-all")
    public ResponseEntity<List<CourseInfoResponse>> findAllCourses() {
        List<CourseInfoResponse> courses = courseService.findAllPayload();
        System.out.println(courses.size());
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/search")
    public ResponseEntity<List<User>> customUserSearch(@RequestBody UserSearchRequest searchRequest) {
        List<User> users = userService.findAll(searchRequest);
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @PutMapping("/user/decline/{id}")
    public ResponseEntity<?> declineUser(@PathVariable Long id) {
        userService.removeByIdNotActivate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest updateRequest) {
        userService.adminUpdateUser(id, updateRequest);
        return null;
    }

}
