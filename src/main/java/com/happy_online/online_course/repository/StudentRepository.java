package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    void deleteByUsername(String username);
}

