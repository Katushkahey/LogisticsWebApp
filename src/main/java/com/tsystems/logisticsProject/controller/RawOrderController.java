package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.service.impl.RawOrderSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/create_order")
public class RawOrderController {

    private ObjectMapper objectMapper;
    private RawOrderSessionService rawOrderService;
    private TruckService truckService;
    private CityService cityService;

    @Autowired
    public void setDependencies(RawOrderSessionService rawOrderService, TruckService truckService,
                                CityService cityService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rawOrderService = rawOrderService;
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @GetMapping
    public String returnPageToCreateOrder(Model model) {
        model.addAttribute("order", rawOrderService.getOrderDto());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "order_create_page";
    }

    @PostMapping(value = "/add_loading_waypoint", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addLoadingWaypoint(HttpServletRequest request) {
        try {
            NewOrderWaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), NewOrderWaypointDto.class);
            rawOrderService.addLoadingWaypoint(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/add_unloading_waypoint", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addUnloadingWaypoint(HttpServletRequest request) {
        try {
            NewOrderWaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), NewOrderWaypointDto.class);
            rawOrderService.addUnloadingWaypoint(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/edit_waypoint", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editWaypoint(HttpServletRequest request) {
        try {
            NewOrderWaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), NewOrderWaypointDto.class);
            rawOrderService.editWaypoint(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @GetMapping("delete_waypoint")
    public String deleteWaypoint(@RequestParam("id") Long id) {
        try {
            rawOrderService.deleteWaypointById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/create_order";
    }

    @GetMapping("save_order")
    public String saveOrder() {
        rawOrderService.saveOrder();
        return "redirect:/order/info";
    }

    @GetMapping("clear_all")
    public String clearAll() {
        rawOrderService.clearAll();
        return "redirect:/order/info";
    }
}
