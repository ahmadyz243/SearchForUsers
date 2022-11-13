package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class User extends BaseDomain<Long> {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    private String name;

    
    @NotBlank
    private String lastname;

    @Column(unique = true)
    @NotBlank
    @Size(max = 20)
    private String nationalCode;

    @NotBlank
    @Size(max = 120)
    private String password;

    private Boolean enabled = false;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String nationalCode, String password, String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.nationalCode = nationalCode;
        this.password = password;
    }

    public User(String username, String name, String lastname, String nationalCode, String password, Boolean enabled, Set<Role> roles) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.nationalCode = nationalCode;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
}
