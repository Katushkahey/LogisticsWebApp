package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.abstraction.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/info-2")
    public String assignedOrders(Model model) {
        model.addAttribute("mapOfOrders", orderService.findAssignedOrders());
        return "orders2_page";
    }

    @GetMapping("/info-3")
    public String completedOrders(Model model) {
        model.addAttribute("mapOfOrders", orderService.findCompletedOrders());
        return "orders3_page";
    }

    @GetMapping("/show_info/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", id);
        model.addAttribute("waypoints", orderService.findWaypointsForCurrentOrderById(id));
        return "order_details_page";
    }

    @GetMapping("/info")
    public String OrdersInfo(Model model) {
        model.addAttribute("mapOfOrders", orderService.findUnassignedOrders());
        return "orders_page";
    }
}
