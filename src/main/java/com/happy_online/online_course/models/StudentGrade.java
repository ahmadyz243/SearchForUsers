package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGrade extends BaseDomain<Long> {
    @ManyToOne
    private Exam exam;
    private Double score = 0D;
    @ManyToOne
    private Student student;
    @OneToOne
    private StudentAnswers studentAnswers;
    private Boolean isAutoSet = false;
    public void setScore(Double score) {
        this.score = this.score + score;
    }
}
