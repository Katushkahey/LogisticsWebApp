package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverPageController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String driverPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("waypoints", driverService.getListOfWaypointsFromPrincipal(username));
        model.addAttribute("driver", driverService.getDriverByPrincipalName(username));
        model.addAttribute("partner", driverService.getPartnerFromPrincipal(username));

        return "driver_menu";
    }

    @GetMapping("/edit_telephoneNumber/{id}")
    public String editTelephone(@PathVariable("id") Long id, @RequestParam("telephone") String telephoneNumber) {
        if (driverService.checkEditedTelephoneNumber(telephoneNumber, id)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
        driverService.update(id, telephoneNumber);

        return "redirect:/driver";
    }


}
