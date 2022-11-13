package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.request.CreateCourseRequest;
import com.happy_online.online_course.payload.response.CourseInfoResponse;
import com.happy_online.online_course.payload.response.CourseInfoResponseTeacher;
import com.happy_online.online_course.payload.response.ViewCoursesResponse;
import com.happy_online.online_course.repository.CourseRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Long, CourseRepository> implements CourseService {
    public CourseServiceImpl(CourseRepository repository, TeacherService teacherService, StudentService studentService) {
        super(repository);
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    final TeacherService teacherService;
    final StudentService studentService;

    @Override
    @Transactional
    public Course saveCourse(Teacher teacher, CreateCourseRequest courseRequest) {
        Course course = convertRequestToCourse(courseRequest);
        course.setTeacher(teacher);
        return repository.save(course);
    }

    @Override
    public void addTeacher(Long courseId, Long TeacherId) {
        Course course = findById(courseId);
        if (course.getTeacher() == null) {
            Teacher teacher = teacherService.findById(TeacherId);
            course.setTeacher(teacher);
            // TODO: 11/4/2022 change the exception 
        } else throw new BadCredentialsException("course already has a teacher");
    }

    @Override
    public void removeTeacher(Long course_id) {
        Optional<Course> course = repository.findById(course_id);
        if (course.isPresent()) {
            course.get().setTeacher(null);
            repository.save(course.get());
        } else {
            // TODO: 11/4/2022 change exception 
            throw new NotFoundException("course not found");
        }
    }

    @Override
    public void addStudent(Long course_id, Long student_id) {
        Student student = studentService.findById(student_id);
        Course course = findById(course_id);
        if (!course.getStudentList().contains(student)) {
            course.setStudent(student);
        } else {
            // TODO: 11/4/2022 change exception 
            throw new NotFoundException("student already exist in this course");
        }
    }

    @Override
    public void removeStudent(Long course_id, Long student_id) {
        Student student = studentService.findById(student_id);
        Course course = findById(course_id);
        if (course.getStudentList().contains(student)) {
            course.getStudentList().remove(student);
        } else {
            throw new NotFoundException("course doesnt contain this student");
        }
    }

    @Override
    public List<ViewCoursesResponse> findAllPayload() {
        List<Course> courses = findAll();
        List<ViewCoursesResponse> viewCoursesResponses = new ArrayList<>();
        courses.forEach(course -> {
            ViewCoursesResponse courseDto = new ViewCoursesResponse();
            BeanUtils.copyProperties(course, courseDto);
            courseDto.setId(course.getId());
            viewCoursesResponses.add(courseDto);
        });
        return viewCoursesResponses;
    }

    @Override
    public List<Course> findByTeacher(Teacher teacher) {
        return repository.findByTeacherOrderByIsActive(teacher);
    }

    @Override
    public List<CourseInfoResponseTeacher> mapCourseToResponse(Teacher teacher) {
        List<Course> course = repository.findByTeacherOrderByIsActive(teacher);
        CourseInfoResponseTeacher courseInfoResponse = new CourseInfoResponseTeacher();
        List<CourseInfoResponseTeacher> courseInfoResponses = new ArrayList<>();
        course.forEach(current -> courseInfoResponses.add(mapCourseToResponseSolver(current)));
        return courseInfoResponses;
    }

    private CourseInfoResponseTeacher mapCourseToResponseSolver(Course course) {
        CourseInfoResponseTeacher courseInfoResponse = new CourseInfoResponseTeacher();
        courseInfoResponse.setId(course.getId());
        courseInfoResponse.setIsActive(course.getActive());
        BeanUtils.copyProperties(course, courseInfoResponse);
        return courseInfoResponse;
    }

    private CourseInfoResponse convertCourseToResponse(Course course) {
        CourseInfoResponse courseInfoResponse = new CourseInfoResponse();
        BeanUtils.copyProperties(course, courseInfoResponse);
        courseInfoResponse.setIsActive(course.getActive());
        return courseInfoResponse;
    }

    private Course convertRequestToCourse(CreateCourseRequest courseRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(courseRequest, course);
        return course;
    }
}
