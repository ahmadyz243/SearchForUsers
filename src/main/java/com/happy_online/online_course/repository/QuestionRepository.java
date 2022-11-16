package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Question;
import com.happy_online.online_course.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTeacherAndCourse(Teacher teacher, Course course);
}
