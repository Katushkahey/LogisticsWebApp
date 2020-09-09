package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverController {

    private DriverService driverService;
    private WaypointService waypointService;

    @Autowired
    public void setDependencies(DriverService driverService, WaypointService waypointService) {
        this.driverService = driverService;
        this.waypointService = waypointService;
    }

    @GetMapping("")
    public String driverPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("driverState", DriverState.values());
        model.addAttribute("driver", driverService.getDriverByPrincipalName(username));

        return "driver_menu";
    }

//    @GetMapping("/edit_telephoneNumber/{id}")
//    public String editTelephone(@PathVariable("id") Long id, @RequestParam("telephone") String telephoneNumber) {
//        if (driverService.checkEditedTelephoneNumber(telephoneNumber, id)) {
//            return "error"; //водитель с таким номером телефона уже существует
//        }
//        DriverDto driverDto = new DriverDto();
//        driverDto.setId(id);
//        driverDto.setTelephoneNumber(telephoneNumber);
//        driverService.update(driverDto);
//        return "redirect:/driver";
//    }

    @PostMapping(value = "/edit_telephoneNumber" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String editTruck(@RequestBody MultiValueMap<String, String> formData) {
        System.out.println(formData);
        return "redirect:/driver";
    }

    @GetMapping("/edit_state/{id}")
    public String editState(@PathVariable("id") Long id, @RequestParam("state") DriverState state) {
        driverService.editState(id, state);
        return "redirect:/driver";
    }

    @GetMapping("/start_order/{id}")
    public String startOrder(@PathVariable("id") Long id) {
        driverService.startOrder(id);
        return "redirect:/driver";
    }

    @GetMapping("/complete_waypoint/{id}")
    public String completeWaypoint(@PathVariable("id") Long id) {
        waypointService.makeCompletedById(id);
        return "redirect:/driver";
    }

    @GetMapping("/finish_order/{id}")
    public String finishOrder(@PathVariable("id") Long id) {
//        driverService.finishOrder(id);
        return "redirect:/driver";
    }

}
