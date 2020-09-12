package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/truck")
public class AdminTrucksController {

    private TruckService truckService;
    private CityService cityService;
    private ObjectMapper objectMapper;

    @Autowired
    public void setDependencies(TruckService truckService, CityService cityService, ObjectMapper objectMapper) {
        this.truckService = truckService;
        this.cityService = cityService;
        this.objectMapper = objectMapper;
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

    @PostMapping(value = "/edit_truck", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, headers = "Accept=*/*"
            , produces = MediaType.APPLICATION_JSON_VALUE) @ResponseBody
    public String editTruck(HttpServletRequest request) {
        try {
            TruckDto truckDto = objectMapper.readValue(request.getInputStream(), TruckDto.class);
            truckService.update(truckDto);
            return "{\"success\":1}";
        }
        catch (Exception e) {
            return "{\"error \":" + e.getMessage() + "}";
        }
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
