package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    void deleteByUsername(String username);

    Optional<Teacher> findByUsername(String username);


}
