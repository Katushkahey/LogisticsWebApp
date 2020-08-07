package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.implementation.TruckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/truck")
public class TruckController {

    @Autowired
    TruckServiceImpl truckServiceImpl;

    @GetMapping("/create_truck")
    public String createTruck(Model model) {

        return "";
    }

    @GetMapping("/edit_truck/{id}")
    public String editTruck(Model model) {

        return "";
    }

    @GetMapping("/delete_truck/{id}")
    public String deleteTruck(@PathVariable("id") Long id) {
        truckServiceImpl.deleteById(id);
        return "redirect:/admin/trucks-info";
    }
}
