package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.StudentGrade;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.payload.response.StudentDto;
import com.happy_online.online_course.repository.StudentRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Long, StudentRepository> implements StudentService {
    public StudentServiceImpl(StudentRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }


    final UserRepository userRepository;

    @Lazy
    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private CourseService courseService;

    @Override
    @Transactional
    public void saveStudentWithUser(SignupRequest signupRequest, User user) {
        userRepository.save(user);
        Student student = mapSignUpRequestToStudent(signupRequest);
        student.setUser(user);
        repository.save(student);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public List<StudentDto> findStudentsNotInCourse(Long courseId) {

        List<StudentDto> studentDtoList = new ArrayList<>();
        List<Student> students = repository.findByCourseListNotContaining(courseService.findById(courseId));
        students.forEach(student -> {
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            BeanUtils.copyProperties(student, studentDto);
            studentDtoList.add(studentDto);
        });
        return studentDtoList;
    }

    @Override
    public Student findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
    }

    public Student mapSignUpRequestToStudent(SignupRequest signupRequest) {
        Student student = new Student();
        BeanUtils.copyProperties(signupRequest, student);
        return student;
    }
}