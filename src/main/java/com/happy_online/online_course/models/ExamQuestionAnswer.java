package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionAnswer extends BaseDomain<Long> {
    private String Answer;
    private Double earnedScore = 0D;
    @ManyToOne
    private ExamQuestion examQuestion;
    @ManyToOne(cascade = CascadeType.MERGE)
    private StudentAnswers studentAnswers;
}
