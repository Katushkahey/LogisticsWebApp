package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.dto.OrderAdminDto;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.service.*;
import com.tsystems.logisticsProject.service.impl.OrderAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class OrderController {

    private OrderService orderService;
    private CityService cityService;
    private TruckService truckService;
    private OrderAssignmentService orderAssignmentService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setDependencies(OrderService orderService, CityService cityService, TruckService truckService,
                                OrderAssignmentService orderAssignmentService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.cityService = cityService;
        this.truckService = truckService;
        this.orderAssignmentService = orderAssignmentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/order/info-2")
    public String getWaytingOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfWaitingOrders());
        return "orders_wayting_page";
    }

    @GetMapping("/order/info-3")
    public String getCompletedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfCompletedOrders());
        return "orders_completed_page";
    }

    @GetMapping("/order/info-4")
    public String getOrdersInProgress(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfOrdersInProgress());
        return "orders_in_progress_page";
    }

    @GetMapping("/order/info")
    public String getAnassignedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.getListOfUnassignedOrders());
        return "orders_no_assigned_page";
    }

    @GetMapping("/order/show_info/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("listOfCities", cityService.getListOfCities());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        return "order_details_page";
    }

    @GetMapping("/order/delete_order/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteById(id);
        return "redirect:/order/info";
    }

    @GetMapping("/order/assign_order/{id}")
    public String assignOrder(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("listOfCombinations", orderAssignmentService.createListOfCombinationsForOrder(id));
        return "assign_order_page";
    }

    @GetMapping("/order/assign_order/choose_assignment/{orderId}/{combinationId}")
    public String saveOrder(@PathVariable("orderId") Long orderId,
                            @PathVariable("combinationId") int combinationId) {
        OrderAdminDto orderDto = orderService.findById(orderId);
        CombinationForOrderDto cfo = orderAssignmentService.getCombinationForOrderByIndex(combinationId - 1, orderDto.getNumber());
        orderService.assign(orderDto, cfo);
        return "redirect:/order/info";
    }

    @GetMapping("/order/cancel_assignment/{id}")
    public String cancelAssignment(@PathVariable("id") Long id) {
        OrderAdminDto orderDto = orderService.findById(id);
        orderService.cancelAssignment(orderDto);
        return "redirect:/order/info-2";
    }

    @PostMapping(value = "driver/change_order_status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
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

}
