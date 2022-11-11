package com.happy_online.online_course.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AdminCo {
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin-menu")
    public String admin() {
        return "views/admin-menu";
    }

    //    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/master-menu")
    public String master() {

        return "views/master-menu";
    }

    @GetMapping("/signup")
    public String signup() {

        return "views/signup";
    }
}
