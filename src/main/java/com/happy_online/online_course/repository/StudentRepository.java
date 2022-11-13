package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    void deleteByUsername(String username);

    List<Student> findByCourseListNotContaining(Course course);
}

