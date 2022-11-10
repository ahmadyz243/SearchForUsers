package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
    //it has to change because I just want to save clock in it
    @Column(nullable = false, name = "examTime")
    private LocalDateTime time;
}
