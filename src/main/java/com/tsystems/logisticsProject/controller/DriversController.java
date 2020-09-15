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
import java.security.Principal;
import java.util.logging.Logger;

@Controller
@RequestMapping
public class DriversController {

    private ObjectMapper objectMapper;

    private OrderService orderService;
    private DriverService driverService;
    private CityService cityService;

    private static final Logger LOG = Logger.getLogger(DriversController.class.getName());

    @Autowired
    public void setDependencies(DriverService driverService, CityService cityService, OrderService orderService,
                                ObjectMapper objectMapper) {
        this.driverService = driverService;
        this.orderService = orderService;
        this.cityService = cityService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/drivers/info")
    public String driversInfo(Model model) {
        model.addAttribute("listOfDrivers", driverService.getListOfDrivers());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "drivers_page";
    }

    @GetMapping("/drivers/delete_driver/{id}")
    public String deleteDriver(@PathVariable("id") Long id, Model model) {
        driverService.deleteById(id);
        return "redirect:/drivers/info";
    }

    @PostMapping(value = "/drivers/create_driver", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createDriver(HttpServletRequest request) {
        try {
            DriverAdminDto driverAdminDto = objectMapper.readValue(request.getInputStream(), DriverAdminDto.class);
            driverService.checkTelephoneNumberToCreateDriver(driverAdminDto.getTelephoneNumber());
            driverService.checkUserNameToCreateDriver(driverAdminDto.getUserName());
            driverService. createNewUser(driverAdminDto.getUserName());
            LOG.info("new user created");
            driverService.add(driverAdminDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return "{\"error \":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/drivers/edit_driver", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editDriver(HttpServletRequest request) {
        try {
            DriverAdminDto driverAdminDto = objectMapper.readValue(request.getInputStream(), DriverAdminDto.class);
            driverService.update(driverAdminDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error \":" + e.getMessage() + "}";
        }
    }

    @GetMapping("/driver")
    public String showDriverPage(Principal principal, Model model) {
        DriverDto driverDto = driverService.getDriverByPrincipalName(principal.getName());
        model.addAttribute("driverState", DriverState.values());
        model.addAttribute("driver", driverDto);
        if (driverDto.getOrderNumber() != null) {
            model.addAttribute("order", orderService.findByNumber(driverDto.getOrderNumber()));
        }

        return "driver_menu";
    }

    @PostMapping(value = "/driver/edit_telephoneNumber", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, headers = "Accept=*/*"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editTelephone(HttpServletRequest request) {
        try {
            DriverDto driverDto = objectMapper.readValue(request.getInputStream(), DriverDto.class);
            driverService.update(driverDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/driver/edit_state", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editState(HttpServletRequest request) {
        try {
            DriverDto driverDto = objectMapper.readValue(request.getInputStream(), DriverDto.class);
            driverService.update(driverDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

}
