package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

}
