package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.service.abstraction.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/truck")
public class TruckController {

    @Autowired
    TruckService truckService;

    @GetMapping("/info")
    public String trucksInfo(Model model) {
        model.addAttribute("listOfTrucks", truckService.getListOfTrucks());
        return "trucks_page";
    }

    @GetMapping("/delete_truck/{id}")
    public String deleteTruck(@PathVariable("id") Long id, Model model) {
        truckService.deleteById(id);
        return "redirect:/truck/info";
    }

    @GetMapping("/edit_truck")
    public String editTruck(@RequestParam(name = "id") Long id, @RequestParam(name = "number") String number,
                            @RequestParam(name = "capacity") Integer capacity, @RequestParam(name = "crew")
                                        Integer crew, @RequestParam(name = "state")TruckState state, Model model) {
        Truck truck = truckService.findById(id);
        truck.setNumber(number);
        truck.setCapacity(capacity);
        truck.setCrewSize(crew);
        truck.setTruckState(state);
        truckService.update(truck);
        model.addAttribute("listOfTrucks", truckService.getListOfTrucks());
        return "redirect:/truck/info";
    }

    @GetMapping("/create_truck")
    public String createTruck(@RequestParam(name = "number") String number, @RequestParam(name = "capacity") Integer capacity,
                              @RequestParam(name = "crew") Integer crew, @RequestParam(name = "state")TruckState state,
                              @RequestParam(name = "city")Long cityId, Model model) {

        if (truckService.findByNumber(number)) {
            return "error";
        } else {
            truckService.add(number, crew, capacity, state, cityId);
        }
        return "redirect:/truck/info";
    }


}
