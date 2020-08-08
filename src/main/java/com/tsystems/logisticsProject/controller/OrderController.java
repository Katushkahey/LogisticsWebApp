package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.implementation.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/info-2")
    public String assignedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.findAssignedOrders());
        return "orders2_page";
    }

    @GetMapping("/info-3")
    public String completedOrders(Model model) {
        model.addAttribute("listOfOrders", orderService.findCompletedOrders());
        return "orders3_page";
    }
}
