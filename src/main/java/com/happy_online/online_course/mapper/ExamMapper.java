package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;
import com.happy_online.online_course.payload.response.ExamQuestionResponse;
import com.happy_online.online_course.payload.response.ExamResponseForView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface ExamMapper {
    ExamResponseForView examsToExamResponseForViewList(Exam exam);

    ExamQuestionResponse examQuestionToExamQuestionResponse(ExamQuestion examQuestion);
}
