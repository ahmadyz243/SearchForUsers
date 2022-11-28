package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExam(Exam exam);
}
