package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Question extends BaseDomain<Long> {
    @ManyToOne
    private Course course;
    @ManyToOne
    private Teacher teacher;

    @Size(min = 2, max = 40)
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String question;
}
