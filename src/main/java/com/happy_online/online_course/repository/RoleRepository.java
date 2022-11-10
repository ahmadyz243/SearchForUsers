package com.happy_online.online_course.repository;

import com.happy_online.online_course.models.ERole;
import com.happy_online.online_course.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
