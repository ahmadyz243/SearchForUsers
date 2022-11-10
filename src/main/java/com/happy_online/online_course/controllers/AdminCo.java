package com.happy_online.online_course.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCo {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin-menu")
    public String admin() {

        return "views/admin-menu";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/master-menu")
    public String master() {

        return "views/master-menu";
    }

    @GetMapping("/signup")
    public String signup() {

        return "views/signup";
    }
//    @PostMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(new MessageResponse("You've been signed out!"));
//    }
}
