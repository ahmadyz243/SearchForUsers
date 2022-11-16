package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestion extends BaseDomain<Long> {

    @JoinColumn(nullable = false)
    @OneToOne
    private Question question;

    @Column(nullable = false)
    private Double score;
    @ManyToOne
    private Exam exam;
}
