package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.implementation.DriverServiceImpl;
import com.tsystems.logisticsProject.service.implementation.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    DriverServiceImpl driverService;

    @Autowired
    OrderServiceImpl orderService;

    @GetMapping("")
    public String driverPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("waypoints", driverService.getListOfWaypointsFromPrincipal(username));
        model.addAttribute("name", driverService.getDriverNameFromPrincipal(username));
        model.addAttribute("telephoneNumber", driverService.getTelephoneNumberFromPrincipal(username));
        model.addAttribute("surname", driverService.getDriverSurnameFromPrincipal(username));
        model.addAttribute("order", driverService.getOrderIdFromPrincipal(username));
        model.addAttribute("partner", driverService.getPartnerFromPrincipal(username));
        model.addAttribute("truck", driverService.getCurrentTruckNumberFromPrincipal(username));

        return "driver_menu";
    }
}
