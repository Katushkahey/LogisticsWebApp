package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/driver")
public class DriverController {

    private ObjectMapper objectMapper;
    private DriverService driverService;
    private WaypointService waypointService;
    private OrderService orderService;

    @Autowired
    public void setDependencies(DriverService driverService, WaypointService waypointService, OrderService orderService,
                                ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.driverService = driverService;
        this.waypointService = waypointService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showDriverPage(Principal principal, Model model) {
        DriverDto driverDto = driverService.getDriverByPrincipalName(principal.getName());
        model.addAttribute("driverState", DriverState.values());
        model.addAttribute("driver", driverDto);
        if (driverDto.getOrderNumber() != null) {
            model.addAttribute("order", orderService.findByNumber(driverDto.getOrderNumber()));
        }

        return "driver_menu";
    }

    @PostMapping(value = "/edit_telephoneNumber", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, headers = "Accept=*/*"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editTelephone(HttpServletRequest request) {
        try {
            DriverDto driverDto = objectMapper.readValue(request.getInputStream(), DriverDto.class);
            driverService.update(driverDto);

            return "{\"success\":1}";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @PostMapping(value = "/edit_state", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editState(HttpServletRequest request) {
        try {
            DriverDto driverDto = objectMapper.readValue(request.getInputStream(), DriverDto.class);
            driverService.update(driverDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/change_order_status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String start_order(HttpServletRequest request) {
        try {
            OrderDriverDto orderDriverDto = objectMapper.readValue(request.getInputStream(), OrderDriverDto.class);
            orderService.update(orderDriverDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @GetMapping("/complete_waypoint/{id}")
    public String completeWaypoint(@PathVariable("id") Long id) {
        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setId(id);
        waypointService.update(waypointDto);
        return "redirect:/driver";
    }
}
