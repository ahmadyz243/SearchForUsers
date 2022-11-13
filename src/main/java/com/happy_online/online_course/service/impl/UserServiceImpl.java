package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.models.Role;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.UserSearchRequest;
import com.happy_online.online_course.payload.request.UserUpdateRequest;
import com.happy_online.online_course.payload.response.UserInfoResponse;
import com.happy_online.online_course.repository.RoleRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.TeacherService;
import com.happy_online.online_course.service.UserService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserRepository> implements UserService {
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, TeacherService teacherService) {
        super(repository);
        this.roleRepository = roleRepository;
        this.teacherService = teacherService;
    }

    final RoleRepository roleRepository;
    private StudentService studentService;
    final TeacherService teacherService;

    @Lazy
    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public List<UserInfoResponse> notEnabledUsers(Boolean bool) {
        List<User> users = repository.findByEnabled(bool);
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        users.forEach(user -> userInfoResponses.add(mapUserToUserInfoResponse(user)));
        return userInfoResponses;
    }

    @Override
    public void activeById(Long id) {
        boolean flag = false;
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            if (!user.get().getEnabled()) {
                User searchedUser = user.get();
                searchedUser.setEnabled(true);
                repository.save(searchedUser);
                flag = true;
            }
        }
        if (!flag) {
            // TODO: 11/3/2022 change the exception 
            throw new BadCredentialsException("user not found or already activated");
        }
    }

    @Override
    public List<User> findAll(UserSearchRequest userSearch) {
        List<Role> roles = new ArrayList<>(userSearch.getRoles());
        return repository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            setName(predicates, root, criteriaBuilder, userSearch.getName());
            setLastname(predicates, root, criteriaBuilder, userSearch.getLastname());
            setRole(predicates, root, criteriaBuilder, roles.get(0));
            setNationalCode(predicates, root, criteriaBuilder, userSearch.getNationalCode());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public void removeByIdNotActivate(Long id) {
        User user = findById(id);
        if (!user.getEnabled()) {
            Set<Role> roleSet = user.getRoles();
            List<Role> roleList = new ArrayList<>(roleSet);
            Role role = roleList.get(0);
            switch (role.getName()) {
                case ROLE_STUDENT -> studentService.deleteByUsername(user.getUsername());
                case ROLE_TEACHER -> teacherService.deleteByUsername(user.getUsername());
            }

        } else {
            // TODO: 11/10/2022 change exception
            throw new BadCredentialsException("user id is wrong");
        }
    }

    @Override
    public User adminUpdateUser(Long id, UserUpdateRequest updateRequest) {
        User user = findById(id);
        if (user.getEnabled()) {

        } else {
            if (updateRequest.getRoles().contains("nujl")) ;
        }
        return null;
    }

    private void setRole(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, Role role) {

        if (role != null && !ObjectUtils.isEmpty(role.getName())) {
            Join<Role, User> userRole = root.join("roles");
            predicates.add(
                    criteriaBuilder.equal(userRole.get("name"), role.getName())
            );
        }
    }

    private void setLastname(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, String lastname) {
        if (lastname != null && !lastname.isBlank()) {
            predicates.add(
                    criteriaBuilder.like(root.get("lastname"), "%" + lastname + "%")
            );
        }
    }

    private void setName(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, String name) {
        if (name != null && !name.isBlank()) {
            predicates.add(
                    criteriaBuilder.like(root.get("name"), "%" + name + "%")
            );
        }
    }

    private void setNationalCode(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, String nationalCode) {
        if (nationalCode != null && !nationalCode.isBlank()) {
            predicates.add(
                    criteriaBuilder.like(root.get("nationalCode"), "%" + nationalCode + "%")
            );
        }
    }

    protected UserInfoResponse mapUserToUserInfoResponse(User user) {
        List<Role> roles = new ArrayList<>(user.getRoles());
        List<String> stringRoles = new ArrayList<>();
        roles.forEach(role -> stringRoles.add(role.getName().name()));
        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getNationalCode(),
                stringRoles,
                user.getName(),
                user.getLastname()
        );
    }
}
