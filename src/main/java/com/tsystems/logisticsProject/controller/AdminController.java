package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.implementation.DriverServiceImpl;
import com.tsystems.logisticsProject.service.implementation.TruckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    TruckServiceImpl truckServiceImpl;

    @Autowired
    DriverServiceImpl driverServiceImpl;

    @GetMapping("")
    public String adminPage() {
        return "admin_menu";
    }

    @GetMapping("/trucks-info")
    public String trucksInfo(Model model) {
        model.addAttribute("listOfTrucks", truckServiceImpl.getListOfTrucks());
        return "trucks_page";
    }

    @GetMapping("/drivers-info")
    public String driversInfo(Model model) {
        model.addAttribute("listOfDrivers", driverServiceImpl.getListOfDrivers());
        return "drivers_page";
    }

    @GetMapping("/orders-info")
    public String OrdersInfo() {
        return "orders_page";
    }

    @GetMapping("/cargoes-info")
    public String CargoesInfo() {
        return "cargoes_page";
    }
}
