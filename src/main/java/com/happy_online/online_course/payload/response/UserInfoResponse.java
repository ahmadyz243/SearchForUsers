package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.Role;

import java.util.List;
import java.util.Set;

public class UserInfoResponse {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String nationalCode;

    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, String nationalCode, List<String> roles,String name,String lastname) {
        this.id = id;
        this.username = username;
        this.nationalCode = nationalCode;
        this.roles = roles;
        this.name=name;
        this.lastname=lastname;
    }

    public UserInfoResponse(List<String> roles) {
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, String nationalCode) {
        this.id = id;
        this.username = username;
        this.nationalCode = nationalCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
