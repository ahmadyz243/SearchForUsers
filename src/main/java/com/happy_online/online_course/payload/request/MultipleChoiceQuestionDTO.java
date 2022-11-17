package com.happy_online.online_course.payload.request;

import com.happy_online.online_course.payload.response.QuestionItemResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleChoiceQuestionDTO {

    private List<QuestionItemResponse> questionItemList;

    private Long courseId;

    private String title;

    private String question;

    private Long examId;
}
