package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.QuestionItem;
import com.happy_online.online_course.payload.response.QuestionItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionItemMapper {
    QuestionItemResponse questionItemToQuestionItemResponse(QuestionItem questionItem);

    QuestionItem questionItemResponseToQuestionItem(QuestionItemResponse response);

    List<QuestionItemResponse> questionItemToQuestionItemResponse(List<QuestionItem> questionItemList);

    List<QuestionItem> questionItemResponseToQuestionItem(List<QuestionItemResponse> responseList);
}
