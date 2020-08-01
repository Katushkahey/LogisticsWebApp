package com.tsystems.logisticsProject.controller;
;
import com.tsystems.logisticsProject.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/")
    public String login() {
        return "index";
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
