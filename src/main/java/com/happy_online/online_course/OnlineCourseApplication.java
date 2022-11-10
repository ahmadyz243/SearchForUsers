package com.happy_online.online_course;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.ERole;
import com.happy_online.online_course.models.Role;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.repository.RoleRepository;
import com.happy_online.online_course.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class OnlineCourseApplication {
    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public OnlineCourseApplication(RoleRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineCourseApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            if (roleRepository.count() == 0) {
                Set<Role> roles = Set.of(
                        new Role(ERole.ROLE_STUDENT),
                        new Role(ERole.ROLE_TEACHER),
                        new Role(ERole.ROLE_ADMIN));
                roleRepository.saveAll(roles);
            }
            if (!userRepository.existsByUsername("admin")) {
                Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
                Role searchedRole = role.orElseThrow(() -> new NotFoundException("database doesn't have any admin call to your backend developer :)"));
                Set<Role> roles = new HashSet<>();
                roles.add(role.get());
                userRepository.save(
                        new User(
                                "admin",
                                "admin",
                                "admin",
                                "admin",
                                passwordEncoder.encode("admin"),
                                true,
                                roles
                        ));
            }
        };
    }
}
