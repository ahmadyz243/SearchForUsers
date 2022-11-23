package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.*;
import com.happy_online.online_course.payload.request.DetailedQuestionDTO;
import com.happy_online.online_course.payload.request.MultipleChoiceQuestionDTO;
import com.happy_online.online_course.payload.response.ExamResponseForView;
import com.happy_online.online_course.payload.response.QuestionItemResponse;
import com.happy_online.online_course.payload.response.QuestionResponse;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionItemMapper.class)
public interface QuestionMapper {
    static QuestionResponse questionToQuestionResponse(Question question) {
        if (question == null) {
            return null;
        }
        QuestionResponse questionResponse = new QuestionResponse();
        if (question instanceof DetailedQuestion) {
            questionResponse.setId(question.getId());
            questionResponse.setQuestion(question.getQuestion());
            questionResponse.setTitle(question.getTitle());
        } else if (question instanceof MultipleChoiceQuestion multipleChoiceQuestion) {
            questionResponse.setId(question.getId());
            questionResponse.setQuestion(question.getQuestion());
            questionResponse.setTitle(question.getTitle());
            questionResponse.setQuestionItemList(questionToQuestionItemResponseList(multipleChoiceQuestion.getQuestionItemList()));
        }
        return questionResponse;
    }

    private static List<QuestionItemResponse> questionToQuestionItemResponseList(List<QuestionItem> questionItem) {
        if (questionItem == null) {
            return null;
        }
        List<QuestionItemResponse> questionItemResponses = new ArrayList<>();
        questionItem.forEach(item -> {
            QuestionItemResponse questionItemResponse = new QuestionItemResponse();
            questionItemResponse.setAnswer(item.getAnswer());
            questionItemResponse.setIsRightAnswer(item.getIsRightAnswer());
            questionItemResponses.add(questionItemResponse);
        });
        return questionItemResponses;
    }

    List<QuestionResponse> questionToQuestionResponseList(List<Question> questions);

    MultipleChoiceQuestion multipleChoiceQuestionDTOtoMultipleChoiceQuestion(MultipleChoiceQuestionDTO dto);

    DetailedQuestion detailedQuestionDTOtoDetailedQuestion(DetailedQuestionDTO question);

}
