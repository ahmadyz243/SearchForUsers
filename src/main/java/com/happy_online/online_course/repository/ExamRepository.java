package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse(Course course);
}
