package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionItemResponse {
    private String answer;
    private Boolean isRightAnswer;
}
