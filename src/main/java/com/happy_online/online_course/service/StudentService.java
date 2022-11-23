package com.happy_online.online_course.service;


import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.payload.response.StudentDto;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface StudentService extends BaseService<Student, Long> {
    void saveStudentWithUser(SignupRequest signupRequest, User user);


    void deleteByUsername(String username);

    List<StudentDto> findStudentsNotInCourse(Long courseId);

    Student findByUsername(String username);
}
