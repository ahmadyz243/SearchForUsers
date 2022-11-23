package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Exam extends BaseDomain<Long> {
    @JoinColumn(nullable = false)
    @ManyToOne
    private Course course;
    @Column(nullable = false, name = "theTitle")
    private String title;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false, name = "startsDate")
    private LocalDateTime startDateAndTime;
    @Column(nullable = false, name = "endDate")
    private LocalDateTime endDate;

    @Column(nullable = false, name = "examTime")
    private Integer time;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamQuestion> examQuestionList = new ArrayList<>();
    @OneToMany(mappedBy = "exam")
    private List<StudentsAnswers> studentsAnswers;

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDateAndTime(LocalDateTime startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setExamQuestionList(ExamQuestion examQuestionList) {
        this.examQuestionList.add(examQuestionList);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
