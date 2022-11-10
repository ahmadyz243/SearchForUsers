package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.repository.TeacherRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends BaseServiceImpl<Teacher, Long, TeacherRepository> implements TeacherService {
    public TeacherServiceImpl(TeacherRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    final UserRepository userRepository;

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

    public Teacher mapSignUpRequestToStudent(SignupRequest signupRequest) {
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(signupRequest, teacher);
        return teacher;
    }
}
