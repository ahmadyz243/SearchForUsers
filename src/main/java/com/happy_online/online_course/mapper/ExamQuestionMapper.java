package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {QuestionMapper.class})
public interface ExamQuestionMapper {

    ExamQuestionResponse examQuestionToExamQuestionResponse(ExamQuestion examQuestion);
}
