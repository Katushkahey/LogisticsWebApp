package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.dto.OrderAdminDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.service.*;
import com.tsystems.logisticsProject.service.impl.OrderAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
public class AdminOrdersController {

    private OrderService orderService;
    private CityService cityService;
    private WaypointService waypointService;
    private TruckService truckService;
    private OrderAssignmentService orderAssignmentService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setDependencies(OrderService orderService, CityService cityService,
                                WaypointService waypointService, OrderAssignmentService orderAssignmentService,
                                TruckService truckService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.cityService = cityService;
        this.waypointService = waypointService;
        this.truckService = truckService;
        this.orderAssignmentService = orderAssignmentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/info-2")
    public String getWaytingOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfWaitingOrders());
        return "orders_wayting_page";
    }

    @GetMapping("/info-3")
    public String getCompletedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfCompletedOrders());
        return "orders_completed_page";
    }

    @GetMapping("/info-4")
    public String getOrdersInProgress(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfOrdersInProgress());
        return "orders_in_progress_page";
    }

    @GetMapping("/info")
    public String getAnassignedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfUnassignedOrders());
        return "orders_no_assigned_page";
    }

    @GetMapping("/show_info/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("listOfCities", cityService.getListOfCities());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        return "order_details_page";
    }

    @GetMapping("/delete_order/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteById(id);
        return "redirect:/order/info";
    }

    @GetMapping("/assign_order/{id}")
    public String assignOrder(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("listOfCombinations", orderAssignmentService.createListOfCombinationsForOrder(id));
        return "assign_order_page";
    }

    @GetMapping("/assign_order/choose_assignment/{orderId}/{combinationId}")
    public String saveOrder(@PathVariable("orderId") Long orderId,
                            @PathVariable("combinationId") int combinationId) {
        OrderAdminDto orderDto = orderService.findById(orderId);
        CombinationForOrderDto cfo = orderAssignmentService.getCombinationForOrderByIndex(combinationId - 1, orderDto.getNumber());
        orderService.assign(orderDto, cfo);
        return "redirect:/order/info";
    }

    @PostMapping(value = "/edit_waypoint/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
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

    @GetMapping("/delete_waypoint/{orderId}/{waypointId}")
    public String deleteWaypoint(@PathVariable("orderId") Long orderId, @PathVariable("waypointId") Long waypointId) {
        if (waypointService.deleteWaypoint(orderId, waypointId)) {
            return "redirect:/order/info";
        }
        return "redirect:/order/show_info/{orderId}";
    }

    @GetMapping("/cancel_assignment/{id}")
    public String cancelAssignment(@PathVariable("id") Long id) {
        OrderAdminDto orderDto = orderService.findById(id);
        orderService.cancelAssignment(orderDto);
        return "redirect:/order/info-2";
    }

}
