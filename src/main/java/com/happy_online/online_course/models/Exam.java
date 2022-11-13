package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

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

    @Column(nullable = false, name = "examTime")
    private Integer time;

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


}
