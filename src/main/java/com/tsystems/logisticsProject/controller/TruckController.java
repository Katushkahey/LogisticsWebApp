package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/truck")
public class TruckController {

    @Autowired
    private TruckService truckService;

    @Autowired
    private CityService cityService;

    @GetMapping("/info")
    public String trucksInfo(Model model) {
        model.addAttribute("listOfTrucks", truckService.getListOfTrucks());
        model.addAttribute("listOfCities", cityService.getListOfCities());
        return "trucks_page";
    }

    @GetMapping("/delete_truck/{id}")
    public String deleteTruck(@PathVariable("id") Long id) {
        truckService.deleteById(id);
        return "redirect:/truck/info";
    }

    @GetMapping("/edit_truck")
    public String editTruck(@RequestParam(name = "id") Long id, @RequestParam(name = "number") String number,
                            @RequestParam(name = "capacity") Double capacity, @RequestParam(name = "crew")
                                    Integer crew, @RequestParam(name = "state") TruckState state,
                            @RequestParam(name = "city") String cityName) {
        if (truckService.checkEditedNumber(number, id)) {
            return "error"; ///фура с таким регистрационным номером уже существует
        }
        truckService.update(id, number, capacity, crew, state, cityName);

        return "redirect:/truck/info";
    }

    @GetMapping("/create_truck")
    public String createTruck(@RequestParam(name = "number") String number, @RequestParam(name = "capacity") Double capacity,
                              @RequestParam(name = "crew") Integer crew, @RequestParam(name = "state") TruckState state,
                              @RequestParam(name = "city") String cityName) {

        if (truckService.findByNumber(number)) {
            return "error";
        } else {
            truckService.add(number, crew, capacity, state, cityName);
        }
        return "redirect:/truck/info";
    }

}
