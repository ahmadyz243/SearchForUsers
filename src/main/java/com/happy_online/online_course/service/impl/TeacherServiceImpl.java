package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.payload.response.StudentDto;
import com.happy_online.online_course.payload.response.TeacherDto;
import com.happy_online.online_course.repository.TeacherRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;

@Service
public class TeacherServiceImpl extends BaseServiceImpl<Teacher, Long, TeacherRepository> implements TeacherService {
    public TeacherServiceImpl(TeacherRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    final UserRepository userRepository;
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void saveTeacherWithUser(SignupRequest signupRequest, User user) {
        userRepository.save(user);
        Teacher teacher = mapSignUpRequestToStudent(signupRequest);
        teacher.setUser(user);
        repository.save(teacher);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public Teacher findByUsername(String username) {
        Optional<Teacher> teacher = repository.findByUsername(username);
        // TODO: 11/10/2022 change the exception
        return teacher.orElseThrow(() -> new NotFoundException("teacher not found with this username"));
    }

    @Override
    public List<Teacher> findAllActives() {

        return null;
    }

    @Override
    public List<TeacherDto> teachersNotInCourse(Long courseId) {
        Course course = courseService.findById(courseId);
        List<Teacher> teachers = repository.findByCourseListNotContaining(course);
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teachers.forEach(teacher -> {
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setId(teacher.getId());
            BeanUtils.copyProperties(teacher, teacherDto);
            teacherDtoList.add(teacherDto);
        });
        return teacherDtoList;
    }

    public Teacher mapSignUpRequestToStudent(SignupRequest signupRequest) {
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(signupRequest, teacher);
        return teacher;
    }
}
