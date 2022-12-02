package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.StudentAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentAnswersRepository extends JpaRepository<StudentAnswers, Long> {
    Optional<StudentAnswers> findByStudentAndExam(Student student, Exam exam);
}
