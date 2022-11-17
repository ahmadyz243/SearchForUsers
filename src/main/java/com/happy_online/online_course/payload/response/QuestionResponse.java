package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String title;
    private String question;
    private List<QuestionItemResponse> questionItemList;
}
