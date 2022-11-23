package com.happy_online.online_course.service;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.request.CreateCourseRequest;
import com.happy_online.online_course.payload.response.CourseInfoResponse;
import com.happy_online.online_course.payload.response.CourseInfoResponseTeacher;
import com.happy_online.online_course.payload.response.ViewCoursesResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface CourseService extends BaseService<Course, Long> {
    Course saveCourse(Teacher teacher,CreateCourseRequest courseRequest);

    void addTeacher(Long courseId, Long TeacherId);

    void removeTeacher(Long course_id);

    void addStudent(Long course_id, Long student_id);

    void removeStudent(Long course_id, Long student_id);

    List<ViewCoursesResponse> findAllPayload();

    List<Course> findByTeacher(Teacher teacher);
    List<CourseInfoResponseTeacher> mapCourseToResponse(Teacher teacher);

    List<CourseInfoResponseForStudent> findStudentCourses(String studentUsername);
}
