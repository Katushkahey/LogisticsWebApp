package com.tsystems.logisticsProject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage(Authentication authentication) {
        if (authentication.toString().equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
            return "redirect:/driver";
        }
    }

    @RequestMapping("/login")
    public String login(@RequestParam(name = "error", required = false) Boolean error, Model model) {
        if(Boolean.TRUE.equals(error)) {
            model.addAttribute("error", "true");
        }
        return "login";
    }

    @GetMapping("/driver")
    public String driverPage() {
        return "driver_menu";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin_menu";
    }
}












