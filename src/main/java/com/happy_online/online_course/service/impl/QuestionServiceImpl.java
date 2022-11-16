package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.models.QuestionItem;
import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.response.QuestionItemResponse;
import com.happy_online.online_course.payload.response.QuestionResponse;
import com.happy_online.online_course.repository.QuestionRepository;
import com.happy_online.online_course.service.QuestionService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question, Long, QuestionRepository> implements QuestionService {

    public QuestionServiceImpl(QuestionRepository repository) {
        super(repository);
    }

    @Override
    public List<QuestionResponse> getCompleteQuestions(Teacher teacher, Course course) {
        List<Question> questions = repository.findByTeacherAndCourse(teacher, course);

        return mapQuestionToResponse(questions);
    }

    private List<QuestionResponse> mapQuestionToResponse(List<Question> questions) {
        List<QuestionResponse> questionResponses = new ArrayList<>();
        questions.forEach(question -> {
            List<QuestionItemResponse> questionItemResponses = new ArrayList<>();
            question.getQuestionItemList().forEach(questionItem -> {
                QuestionItemResponse item = new QuestionItemResponse();
                item.setAnswer(questionItem.getAnswer());
                questionItemResponses.add(item);
            });
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setQuestion(question.getQuestion());
            questionResponse.setQuestionItemList(questionItemResponses);
            questionResponses.add(questionResponse);
        });
        return questionResponses;
    }
}
