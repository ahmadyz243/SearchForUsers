package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.QuestionItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private String question;
    private List<QuestionItemResponse> questionItemList;
}
