package com.happy_online.online_course.service;

import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.UserSearchRequest;
import com.happy_online.online_course.payload.request.UserUpdateRequest;
import com.happy_online.online_course.payload.response.UserInfoResponse;
import com.happy_online.online_course.service.base.BaseService;

import java.util.List;

public interface UserService extends BaseService<User, Long> {
    List<UserInfoResponse> notEnabledUsers(Boolean bool);

    void activeById(Long id);

    List<User> findAll(UserSearchRequest userSearch);

    void removeByIdNotActivate(Long id);

    User adminUpdateUser(Long id, UserUpdateRequest updateRequest);
}
