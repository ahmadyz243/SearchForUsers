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
public class StudentsAnswers extends BaseDomain<Long> {
    @ManyToOne
    private Exam exam;
    @OneToOne
    private Student student;
    @OneToMany(mappedBy = "studentsAnswers")
    private List<ExamQuestionAnswer> examQuestionAnswerList;
    private Long studentGrade;
}
