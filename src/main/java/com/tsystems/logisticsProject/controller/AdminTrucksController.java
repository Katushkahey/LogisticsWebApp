package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/truck")
public class AdminTrucksController {

    private TruckService truckService;
    private CityService cityService;

    @Autowired
    public void setDependencies(TruckService truckService, CityService cityService) {
        this.truckService = truckService;
        this.cityService = cityService;
    }

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
                                    Integer crew, @RequestParam(name = "state") String state,
                            @RequestParam(name = "city") String cityName) {
        if (truckService.checkEditedNumber(number, id)) {
            return "error"; ///фура с таким регистрационным номером уже существует
        }
        TruckDto truckDto = new TruckDto();
        truckDto.setId(id);
        truckDto.setNumber(number);
        truckDto.setCapacity(capacity);
        truckDto.setCrewSize(crew);
        truckDto.setState(state);
        truckDto.setCityName(cityName);
        truckService.update(truckDto);

        return "redirect:/truck/info";
    }

    @GetMapping("/create_truck")
    public String createTruck(@RequestParam(name = "number") String number, @RequestParam(name = "capacity") Double capacity,
                              @RequestParam(name = "crew") Integer crew, @RequestParam(name = "state") String state,
                              @RequestParam(name = "city") String cityName) {

        if (truckService.findByNumber(number)) {
            return "error";
        } else {
            TruckDto truckDto = new TruckDto();
            truckDto.setNumber(number);
            truckDto.setCapacity(capacity);
            truckDto.setCrewSize(crew);
            truckDto.setState(state);
            truckDto.setCityName(cityName);
            truckService.add(truckDto);
        }
        return "redirect:/truck/info";
    }

}
