package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.abstraction.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @Autowired
    DriverService driverService;

    @GetMapping("")
    public String adminPage() {
        return "admin_menu";
    }

}
