package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
    @Column(nullable = false, name = "questionNumber")
    private Integer number;
}
