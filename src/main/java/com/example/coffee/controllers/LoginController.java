package com.example.coffee.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Return the name of the login page (assuming it's a Thymeleaf or JSP template)
    }
}

