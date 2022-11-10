package com.happy_online.online_course.models;

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
public class Teacher extends BaseDomain<Long> {
    @OneToOne(cascade = CascadeType.ALL)
    private User user;


    @OneToMany(mappedBy = "teacher")
    private List<Course> courseList;


    @OneToMany(mappedBy = "teacher")
    private List<Question> questionList;

    @Column(nullable = false, unique = true)
    private String username = getNationalCode();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String nationalCode;
}
