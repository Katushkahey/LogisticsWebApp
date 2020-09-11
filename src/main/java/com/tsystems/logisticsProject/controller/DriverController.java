package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverController {

    private DriverService driverService;
    private WaypointService waypointService;
    private OrderService orderService;

    @Autowired
    public void setDependencies(DriverService driverService, WaypointService waypointService, OrderService orderService) {
        this.driverService = driverService;
        this.waypointService = waypointService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showDriverPage(Principal principal, Model model) {
        DriverDto driverDto = driverService.getDriverByPrincipalName(principal.getName());
        model.addAttribute("driverState", DriverState.values());
        model.addAttribute("driver", driverDto);
        model.addAttribute("order", orderService.findByNumber(driverDto.getOrderNumber()));

        return "driver_menu";
    }

    @GetMapping("/edit_telephoneNumber/{id}")
    public String editTelephone(@PathVariable("id") Long id, @RequestParam("telephone") String telephoneNumber) {
        if (driverService.checkEditedTelephoneNumber(telephoneNumber, id)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
        DriverDto driverDto = new DriverDto();
        driverDto.setId(id);
        driverDto.setTelephoneNumber(telephoneNumber);
        driverService.update(driverDto);
        return "redirect:/driver";
    }

//    @PostMapping(value = "/edit_telephoneNumber" ,consumes = MediaType.APPLICATION_JSON_VALUE)
//    public String editTruck(@RequestBody MultiValueMap<String, String> formData) {
//        System.out.println(formData);
//        return "redirect:/driver";
//    }

    @GetMapping("/edit_state/{id}")
    public String editState(@PathVariable("id") Long id, @RequestParam("state") String  state) {
        DriverDto driverDto = new DriverDto();
        driverDto.setId(id);
        driverDto.setDriverState(state);
        driverService.update(driverDto);
        return "redirect:/driver";
    }

    @GetMapping("/start_order/{id}")
    public String startOrder(@PathVariable("id") Long id) {
        OrderDriverDto orderDriverDto = new OrderDriverDto();
        orderDriverDto.setId(id);
        orderDriverDto.setStatus(OrderStatus.IN_PROGRESS.toString());
        orderService.update(orderDriverDto);
        return "redirect:/driver";
    }

    @GetMapping("/complete_waypoint/{id}")
    public String completeWaypoint(@PathVariable("id") Long id) {
        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setId(id);
        waypointService.update(waypointDto);
        return "redirect:/driver";
    }

    @GetMapping("/finish_order/{id}")
    public String finishOrder(@PathVariable("id") Long id) {
        OrderDriverDto orderDto = new OrderDriverDto();
        orderDto.setId(id);
        orderService.update(orderDto);
        return "redirect:/driver";
    }
}
