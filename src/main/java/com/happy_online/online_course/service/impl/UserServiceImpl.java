package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.ERole;
import com.happy_online.online_course.models.Role;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.UserSearchRequest;
import com.happy_online.online_course.payload.request.UserUpdateRequest;
import com.happy_online.online_course.payload.response.UserInfoResponse;
import com.happy_online.online_course.repository.RoleRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.UserService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserRepository> implements UserService {
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository) {
        super(repository);
        this.roleRepository = roleRepository;
    }

    final RoleRepository roleRepository;

    @Override
    public List<UserInfoResponse> notEnabledUsers(Boolean bool) {
        List<User> users = repository.findByEnabled(bool);
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        users.forEach(user -> userInfoResponses.add(mapUserToUserInfoResponse(user)));
        if (users.isEmpty()) {
            // TODO: 11/3/2022 change exception
            throw new NotFoundException("there is no result for your request");
        }
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
        return repository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            setName(predicates, root, criteriaBuilder, userSearch.getName());
            setLastname(predicates, root, criteriaBuilder, userSearch.getLastname());
            setRole(predicates, root, criteriaBuilder, userSearch.getRoles());
            setNationalCode(predicates, root, criteriaBuilder, userSearch.getNationalCode());
            setUsername(predicates, root, criteriaBuilder, userSearch.getUsername());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public void removeByIdNotActivate(Long id) {
        User user = findById(id);
        if (!user.getEnabled()) {
            repository.delete(user);
        } else {
            throw new BadCredentialsException("user id is wrong");
        }
    }

    @Override
    public User adminUpdateUser(Long id, UserUpdateRequest updateRequest) {
        User user = findById(id);
        if (user.getEnabled()) {

        } else {
    if(updateRequest.getRoles().contains("nujl"));
        }
        return null;
    }

    private void setRole(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, Set<Role> roles) {

        if (roles != null) {
            predicates.add(
                    criteriaBuilder.like(root.get("role"), "%" + roles + "%")
            );
        }
    }

    private void setUsername(List<Predicate> predicates, Root<User> root, CriteriaBuilder criteriaBuilder, String username) {
        if (username != null && !username.isBlank()) {
            predicates.add(
                    criteriaBuilder.like(root.get("username"), "%" + username + "%")
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
