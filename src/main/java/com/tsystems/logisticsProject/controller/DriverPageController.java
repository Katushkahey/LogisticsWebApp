package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
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

    @Autowired
    private WaypointService waypointService;

    @GetMapping("")
    public String driverPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("waypoints", driverService.getListOfWaypointsFromPrincipal(username));
        model.addAttribute("driver", driverService.getDriverByPrincipalName(username));
        model.addAttribute("partners", driverService.getPartnersFromPrincipal(username));
        model.addAttribute("driverState", DriverState.values());

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

    @GetMapping("/edit_state/{id}")
    public String editState(@PathVariable("id") Long id, @RequestParam("state") DriverState state) {
        driverService.editState(id, state);

        return "redirect:/driver";
    }

    @GetMapping("/start_order/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.startOrder(id);
        return "redirect:/driver";
    }

    @GetMapping("/complete_waypoint/{id}")
    public String completeWaypoint(@PathVariable("id") Long id) {
        waypointService.makeCompletedById(id);
        return "redirect:/driver";
    }

    @GetMapping("/finish_order/{id}")
    public String finishOrder(@PathVariable("id") Long id) {
        driverService.finishOrder(id);
        return "redirect:/driver";
    }

}
