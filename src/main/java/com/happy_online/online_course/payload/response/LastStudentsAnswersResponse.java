package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.ExamQuestionAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LastStudentsAnswersResponse {
    private StudentResponse student;
    private List<ExamQuestionStudentAnswersResponse> answers;

    public void setAnswers(ExamQuestionStudentAnswersResponse answers) {
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        }
        this.answers.add(answers);
    }
}
