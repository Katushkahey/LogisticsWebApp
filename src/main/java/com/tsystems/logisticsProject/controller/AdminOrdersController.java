package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.dto.OrderAdminDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.mapper.OrderAdminMapper;
import com.tsystems.logisticsProject.service.*;
import com.tsystems.logisticsProject.service.impl.OrderAssignmentService;
import com.tsystems.logisticsProject.entity.CombinationForOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class AdminOrdersController {

    private OrderService orderService;
    private CityService cityService;
    private WaypointService waypointService;
    private TruckService truckService;
    private OrderAssignmentService orderAssignmentService;

    @Autowired
    public void setDependencies(OrderService orderService, CityService cityService,
                                WaypointService waypointService, OrderAssignmentService orderAssignmentService,
                                TruckService truckService) {
        this.orderService = orderService;
        this.cityService = cityService;
        this.waypointService = waypointService;
        this. truckService = truckService;
        this.orderAssignmentService = orderAssignmentService;
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
    public String OrdersInfo(Model model) {
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
        CombinationForOrderDto cfo = orderAssignmentService.getCombinationForOrderByIndex(combinationId - 1);
        orderService.assign(orderDto, cfo);
        return "redirect:/order/info";
    }

    @GetMapping("/edit_waypoint/{id}")
    public String editWaypoint(@PathVariable("id") Long orderId, @RequestParam("id") Long waypointId,
                               @RequestParam("cargoName") String cargoName, @RequestParam("weight") double weight,
                               @RequestParam("city") String city, Model model) {
        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setId(waypointId);
        waypointDto.setCargoName(cargoName);
        waypointDto.setCargoWeight(weight);
        waypointDto.setCityName(city);
        waypointService.update(waypointDto);

        return "redirect:/order/show_info/{orderId}";
    }

    @GetMapping("/delete_waypoint/{orderId}/{waypointId}")
    public String deleteWaypoint(@PathVariable("orderId") Long orderId, @PathVariable("waypointId") Long waypointId) {
        if (orderService.deleteWaypoint(orderId, waypointId)) {
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
