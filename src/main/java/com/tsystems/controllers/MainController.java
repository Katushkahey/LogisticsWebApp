package com.tsystems.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String startPage() {
        return "index";
    }

    @GetMapping("/navigation")
    public String driverMenu() {
        return  "navigation";
    }

    @GetMapping("/admin/drivers")
    public String drivers(Model model) {
        return "drivers";
    }
}
