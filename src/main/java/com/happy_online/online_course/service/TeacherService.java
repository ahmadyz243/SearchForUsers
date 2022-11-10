package com.happy_online.online_course.service;

import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.service.base.BaseService;

public interface TeacherService extends BaseService<Teacher,Long> {
    void saveTeacherWithUser(SignupRequest signupRequest, User user);
}
