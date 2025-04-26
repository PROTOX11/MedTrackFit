package com.example.medtrackfit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/mentor")
public class MentorController {

        @RequestMapping("/add")
        public String addMentor() {
                return "user/find_mentor";
        }

}
