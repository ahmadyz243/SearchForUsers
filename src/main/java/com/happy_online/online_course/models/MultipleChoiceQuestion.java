package com.happy_online.online_course.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class MultipleChoiceQuestion extends Question {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionItem> questionItemList;
}
