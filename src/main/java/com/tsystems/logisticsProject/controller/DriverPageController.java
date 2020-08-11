package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.abstraction.DriverService;
import com.tsystems.logisticsProject.service.abstraction.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverPageController {

    @Autowired
    DriverService driverService;

    @Autowired
    OrderService orderService;

    @GetMapping("")
    public String driverPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("waypoints", driverService.getListOfWaypointsFromPrincipal(username));
        model.addAttribute("driver", driverService.getDriverByPrincipalName(username));
        model.addAttribute("partner", driverService.getPartnerFromPrincipal(username));

        return "driver_menu";
    }
}