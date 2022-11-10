package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.repository.StudentRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Long, StudentRepository> implements StudentService {
    public StudentServiceImpl(StudentRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    final UserRepository userRepository;


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

    public Student mapSignUpRequestToStudent(SignupRequest signupRequest) {
        Student student = new Student();
        BeanUtils.copyProperties(signupRequest, student);
        return student;
    }
}