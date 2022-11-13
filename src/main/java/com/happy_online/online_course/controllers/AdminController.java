package com.happy_online.online_course.controllers;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.CreateCourseRequest;
import com.happy_online.online_course.payload.request.UserSearchRequest;
import com.happy_online.online_course.payload.request.UserUpdateRequest;
import com.happy_online.online_course.payload.response.*;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    final UserService userService;
    final CourseService courseService;

    final TeacherService teacherService;

    public AdminController(UserService userService, CourseService courseService, TeacherService teacherService) {
        this.userService = userService;
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping("/user/not-actives")
    public ResponseEntity<List<UserInfoResponse>> getNotEnabled() {
        List<UserInfoResponse> userInfoResponses = userService.notEnabledUsers(false);
        return ResponseEntity.ok(userInfoResponses);
    }

    @PutMapping("/user/reject-by-id/{id}")
    public ResponseEntity<?> rejectById(@PathVariable Long id) {
        userService.removeByIdNotActivate(id);
        return ResponseEntity.ok(new MessageResponse("successfully rejected"));
    }

    @PutMapping("/user/active-by-id/{id}")
    public ResponseEntity<?> activeUserById(@PathVariable Long id) {
        userService.activeById(id);
        return ResponseEntity.ok(new MessageResponse("successfully activated"));
    }

    @PostMapping(value = "/course/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest courseRequest) {
        Teacher teacher = teacherService.findById(courseRequest.getMasterId());
        courseService.saveCourse(teacher, courseRequest);

        return new ResponseEntity<>("course created", HttpStatus.CREATED);
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

    @GetMapping("/teacher/find-all-actives")
    public ResponseEntity<List<TeacherResponseAddCourse>> teacherList() {
        List<TeacherResponseAddCourse> teacherDtoList = new ArrayList<>();
        List<Teacher> teachers = teacherService.findAll();
        teachers.forEach(teacher -> {
            if (teacher.getUser().getEnabled()) {
                TeacherResponseAddCourse teacherDto = new TeacherResponseAddCourse();
                BeanUtils.copyProperties(teacher, teacherDto);
                teacherDto.setId(teacher.getId());
                teacherDtoList.add(teacherDto);
            }
        });
        return new ResponseEntity<>(teacherDtoList, HttpStatus.ACCEPTED);
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
    public ResponseEntity<List<ViewCoursesResponse>> findAllCourses() {
        List<ViewCoursesResponse> courses = courseService.findAllPayload();
        System.out.println(courses.size());
        return new ResponseEntity<>(courses, HttpStatus.ACCEPTED);
    }

    @GetMapping("/course/find-by-id/{id}")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long id){
        Course course = courseService.findById(id);
        CourseDto courseDto = new CourseDto();
        courseDto.setId(id);
        BeanUtils.copyProperties(course.getTeacher(), courseDto.getTeacherDto());
        courseDto.getTeacherDto().setId(course.getTeacher().getId());
        course.getStudentList().forEach(student -> {
            StudentDto studentDto = new StudentDto();
            BeanUtils.copyProperties(student, studentDto);
            studentDto.setId(student.getId());
            courseDto.getStudentDtoList().add(studentDto);
        });
        return new ResponseEntity<>(courseDto, HttpStatus.ACCEPTED);
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
