package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.OrderClientDto;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/infoboard")
public class InfoboardController {

    private OrderService orderService;
    private DriverService driverService;
    private TruckService truckService;

    @Autowired
    public InfoboardController(OrderService orderService, DriverService driverService, TruckService truckService) {
        this.orderService = orderService;
        this.driverService = driverService;
        this.truckService = truckService;
    }

    @GetMapping(value = "/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<OrderClientDto> getTopOrders() {
        System.out.println(" Я получаю ордеры");
        return orderService.getTopOrders();
    }

    @GetMapping(value = "/info/drivers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public LinkedHashMap<String, Integer> getDriverStats() {
        System.out.println(" Я получаю водителей");
        return driverService.getDriversInfo();
    }

    @GetMapping(value = "/info/trucks", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public LinkedHashMap<String, Integer> getTruckStats() {
        System.out.println(" Я получаю фуры");
        return truckService.getTrucksInfo();
    }
}
