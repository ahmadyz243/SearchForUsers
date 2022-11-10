package com.happy_online.online_course.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseDomain<Long> {
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;
    @JsonIgnore
    @ManyToMany(mappedBy = "studentList")
    private List<Course> courseList;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false, unique = true)
    private String nationalCode;
}
