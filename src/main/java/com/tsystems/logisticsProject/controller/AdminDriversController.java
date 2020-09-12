package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/drivers")
public class AdminDriversController {

    private ObjectMapper objectMapper;
    private DriverService driverService;
    private CityService cityService;

    @Autowired
    public void setDependencies(DriverService driverService, CityService cityService, ObjectMapper objectMapper) {
        this.driverService = driverService;
        this.cityService = cityService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/info")
    public String driversInfo(Model model) {
        model.addAttribute("listOfDrivers", driverService.getListOfDrivers());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "drivers_page";
    }

    @GetMapping("/delete_driver/{id}")
    public String deleteDriver(@PathVariable("id") Long id, Model model) {
        driverService.deleteById(id);
        return "redirect:/drivers/info";
    }

    @PostMapping(value = "/create_driver", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createDriver(HttpServletRequest request) {
        try {
            DriverAdminDto driverAdminDto = objectMapper.readValue(request.getInputStream(), DriverAdminDto.class);
            driverService.update(driverAdminDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error \":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/edit_driver", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editDriver(HttpServletRequest request) {
        try {
            DriverAdminDto driverAdminDto = objectMapper.readValue(request.getInputStream(), DriverAdminDto.class);
            if (driverService.checkEditedTelephoneNumber(driverAdminDto.getTelephoneNumber(), driverAdminDto.getId())) {
                return "{\"error \": водитель с таким номером телефона уже существует}";
            }
            driverService.update(driverAdminDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error \":" + e.getMessage() + "}";
        }
    }

}
