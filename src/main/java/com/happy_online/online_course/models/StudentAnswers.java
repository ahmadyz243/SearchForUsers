package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentAnswers extends BaseDomain<Long> {
    @ManyToOne
    private Exam exam;
    @OneToOne
    private Student student;
    @OneToMany(mappedBy = "studentAnswers", cascade = CascadeType.ALL)
    private List<ExamQuestionAnswer> examQuestionAnswerList = new ArrayList<>();
    @OneToOne(mappedBy = "studentAnswers")
    private StudentGrade grade;
    private Boolean finalized = false;

    public void setExamQuestionAnswerList(ExamQuestionAnswer examQuestionAnswer) {
        if (this.examQuestionAnswerList == null) {
            this.examQuestionAnswerList = new ArrayList<>();
        }
        this.examQuestionAnswerList.add(examQuestionAnswer);
    }
}
