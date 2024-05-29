package com.two.tumbler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/auth/chooseRole";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "adminHomePage";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "userHomePage";
    }
}
