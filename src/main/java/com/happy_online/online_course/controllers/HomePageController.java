package com.happy_online.online_course.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    @GetMapping("/login")
    public String home() {

        return "views/login";
    }



}
