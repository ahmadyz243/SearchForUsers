package com.happy_online.online_course.payload.request;

import com.happy_online.online_course.models.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UserUpdateRequest {
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @Size(max = 20)
    private String nationalCode;

    private Boolean enabled;

    private Set<Role> roles;
}
