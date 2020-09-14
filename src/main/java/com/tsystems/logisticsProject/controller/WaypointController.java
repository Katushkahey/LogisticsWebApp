package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class WaypointController {

    private WaypointService waypointService;
    private ObjectMapper objectMapper;

    @Autowired
    public void setDependencies(WaypointService waypointService, ObjectMapper objectMApper) {
        this.waypointService = waypointService;
        this.objectMapper = objectMApper;
    }

    @PostMapping(value = "/order/edit_waypoint/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editWaypoint(@PathVariable("id") Long orderId, HttpServletRequest request) {
        try {
            WaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), WaypointDto.class);
            waypointService.update(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {

            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @GetMapping("/order/delete_waypoint/{orderId}/{waypointId}")
    public String deleteWaypoint(@PathVariable("orderId") Long orderId, @PathVariable("waypointId") Long waypointId) {
        if (waypointService.deleteWaypoint(orderId, waypointId)) {
            return "redirect:/order/info";
        }
        return "redirect:/order/show_info/{orderId}";
    }

    @GetMapping("driver/complete_waypoint/{id}")
    public String completeWaypoint(@PathVariable("id") Long id) {
        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setId(id);
        waypointService.update(waypointDto);
        return "redirect:/driver";
    }
}
