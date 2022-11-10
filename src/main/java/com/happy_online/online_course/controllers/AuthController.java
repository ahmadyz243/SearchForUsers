package com.happy_online.online_course.controllers;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.ERole;
import com.happy_online.online_course.models.Role;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.LoginRequest;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.payload.response.MessageResponse;
import com.happy_online.online_course.payload.response.UserInfoResponse;
import com.happy_online.online_course.repository.RoleRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.security.jwt.JwtUtils;
import com.happy_online.online_course.security.services.UserDetailsImpl;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.TeacherService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final AuthenticationManager authenticationManager;
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public final PasswordEncoder encoder;
    public final JwtUtils jwtUtils;
    public final StudentService studentService;
    public final TeacherService teacherService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, StudentService studentService, TeacherService teacherService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        roles).getRoles());
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByNationalCode(signUpRequest.getNationalCode())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: NationalCode is already taken!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getNationalCode(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getName(),
                signUpRequest.getLastname()
        );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            throw new BadCredentialsException("role cant be null");
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_STUDENT":
                        Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new NotFoundException("Error: Role is not found."));
                        roles.add(studentRole);
                        user.setRoles(roles);
                        studentService.saveStudentWithUser(signUpRequest, user);
                        break;
                    case "ROLE_TEACHER":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new NotFoundException("Error: Role is not found."));
                        roles.add(teacherRole);
                        user.setRoles(roles);
                        teacherService.saveTeacherWithUser(signUpRequest, user);
                        break;
                    default:
                        throw new BadCredentialsException("use a valid roles");
                }
            });
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        System.out.println("fuck");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
