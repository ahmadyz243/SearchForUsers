package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.mapper.CourseMapper;
import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.request.CreateCourseRequest;
import com.happy_online.online_course.payload.response.CourseInfoResponse;
import com.happy_online.online_course.payload.response.CourseInfoResponseTeacher;
import com.happy_online.online_course.payload.response.ExamResponseForStudent;
import com.happy_online.online_course.payload.response.ViewCoursesResponse;
import com.happy_online.online_course.repository.CourseRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Long, CourseRepository> implements CourseService {
    public CourseServiceImpl(CourseRepository repository, CourseMapper courseMapper) {
        super(repository);
        this.courseMapper = courseMapper;
    }

    private TeacherService teacherService;
    private StudentService studentService;
    private final CourseMapper courseMapper;

    @Lazy
    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Lazy
    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    @Transactional
    public Course saveCourse(Teacher teacher, CreateCourseRequest courseRequest) {
        Course course = convertRequestToCourse(courseRequest);
        course.setTeacher(teacher);
        return repository.save(course);
    }

    @Override
    @Transactional
    public void addTeacher(Long courseId, Long TeacherId) {
        Course course = findById(courseId);
        Teacher teacher = teacherService.findById(TeacherId);
        // TODO: 11/14/2022 change the exception
        if (teacher == null)
            throw new NotFoundException("teacher not found");
        course.setTeacher(teacher);
        repository.save(course);
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

    @Override
    public List<CourseInfoResponseForStudent> findStudentCourses(String studentUsername) {
        Student student = studentService.findByUsername(studentUsername);
        List<Course> studentCourses = repository.findByStudentListContaining(student);
        List<CourseInfoResponseForStudent> courseResponse = courseMapper.mapCoursesToCourseInfoResponseForStudentList(studentCourses);
        for (int i = 0; i < studentCourses.size(); i++) {
            courseResponse.get(i).setTeacherName(studentCourses.get(i).getTeacher().getName());
            courseResponse.get(i).setId(studentCourses.get(i).getId());
            courseResponse.get(i).getExamList().forEach(exam -> {
                exam.setEnabled(checkForEnableExam(exam));
            });
        }
        return courseResponse;
    }

    private Boolean checkForEnableExam(ExamResponseForStudent exam) {
        if (LocalDateTime.now().isAfter(exam.getStartDateAndTime().minusMinutes(1L)) && LocalDateTime.now().isBefore(exam.getEndDate().plusMinutes(1L))) {
            return true;
        } else
            return false;
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
