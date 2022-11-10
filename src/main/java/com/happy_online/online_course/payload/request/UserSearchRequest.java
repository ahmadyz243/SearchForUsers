package com.happy_online.online_course.payload.request;

import com.happy_online.online_course.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserSearchRequest {
    private String name;
    private String lastname;
    private String username;
    private String nationalCode;
    private Set<Role> roles;
}
