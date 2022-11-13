package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}
