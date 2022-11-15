package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseDomain<Long> {
    @OneToMany(mappedBy = "question")
    private List<QuestionItem> questionItemList;

    @ManyToOne
    private Course course;
    @ManyToOne
    private Teacher teacher;

    @Column(nullable = false)
    private String question;
}
