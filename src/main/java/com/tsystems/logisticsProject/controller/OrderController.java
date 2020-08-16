package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.service.*;
import com.tsystems.logisticsProject.util.OrderAssignmentService;
import com.tsystems.logisticsProject.util.entity.CombinationForOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private TruckService truckService;

    @Autowired
    private WaypointService waypointService;

    @Autowired
    private OrderAssignmentService orderAssignmentService;

    @GetMapping("/info-2")
    public String waytingOrders(Model model) {
        model.addAttribute("listOfWaypoints", orderService.findListOfWaypointsForWaytingOrders());
        model.addAttribute("mapOfOrders", orderService.findWaitingOrders());
        model.addAttribute("mapOfDriversForOrders", orderService.getMapOfDriversForWaitingOrders());
        return "orders_wayting_page";
    }

    @GetMapping("/info-3")
    public String completedOrders(Model model) {
        model.addAttribute("listOfWaypoints", orderService.findListOfWaypointsForCompletedOrders());
        model.addAttribute("mapOfOrders", orderService.findCompletedOrders());
        model.addAttribute("mapOfDriversForOrders", orderService.getMapOfDriversForCompletedOrders());
        return "orders_completed_page";
    }

    @GetMapping("/info-4")
    public String ordersInProgress(Model model) {
        model.addAttribute("listOfWaypoints", orderService.findListOfWaypointsForOrdersInProgress());
        model.addAttribute("mapOfOrders", orderService.findOrdersInProgress());
        model.addAttribute("mapOfDriversForOrders", orderService.getMapOfDriversForOrdersInProgress());
        return "orders_in_progress_page";
    }

    @GetMapping("/show_info/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());
        model.addAttribute("waypoints", orderService.findWaypointsForCurrentOrderById(id));
        model.addAttribute("order_status", orderService.findById(id).getStatus());
        return "order_details_page";
    }

    @GetMapping("/info")
    public String OrdersInfo(Model model) {
        model.addAttribute("listOfWaypoints", orderService.findListOfWaypointsForUnassignedOrders());
        model.addAttribute("mapOfOrders", orderService.findUnassignedOrders());
        model.addAttribute("mapOfDriversForOrders", orderService.getMapOfDriversForUnassignedOrders());
        return "orders_no_assigned_page";
    }

    @GetMapping("/delete_order/{id}")
    public String deleteOrder(@PathVariable("id") Long id, Model model) {
        orderService.deleteById(id);
        model.addAttribute("mapOfOrders", orderService.findUnassignedOrders());
        model.addAttribute("mapOfDriversForOrders", orderService.getMapOfDriversForUnassignedOrders());
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
        CombinationForOrder cf =  orderAssignmentService.getCombinationForOrderByIndex(combinationId - 1);
        orderService.assign(orderId, cf.getTruck(), cf.getListOfDrivers());
        return "redirect:/order/info";
    }

    @GetMapping("/edit_waypoint/{id}")
    public String editWaypoint(@PathVariable("id") Long orderId, @RequestParam("id") Long waypointId,
                               @RequestParam("cargoName") String cargoName, @RequestParam("weight") double weight,
                               @RequestParam("city") String city, Model model) {
        model.addAttribute("id", orderId);
        waypointService.editWaypoint(waypointId, cargoName, weight, city);

        return "redirect:/order/show_info/{id}";
    }

    @GetMapping("/delete_waypoint/{orderId}/{waypointId}")
    public String deleteWaypoint(@PathVariable("orderId") Long orderId, @PathVariable("waypointId") Long waypointId,
                                 Model model) {
        model.addAttribute("id", orderId);
        if(waypointService.deleteWaypoint(orderId, waypointId)) {
            return "redirect:/order/info";
        }

        return "redirect:/order/show_info/{id}";
    }

}
