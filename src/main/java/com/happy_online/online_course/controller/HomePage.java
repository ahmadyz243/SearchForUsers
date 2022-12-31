package com.happy_online.online_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePage {

    @GetMapping("/homepage")
    public String homePage(){
        return "views/homepage";
    }

}
