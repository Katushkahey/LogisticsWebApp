package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/drivers")
public class AdminDriversController {

    private DriverService driverService;
    private CityService cityService;

    @Autowired
    public void setDependencies(DriverService driverService, CityService cityService) {
        this.driverService = driverService;
        this.cityService = cityService;
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

    @GetMapping("/create_driver")
    public String createDriver(@RequestParam("name") String name, @RequestParam("surname") String surname,
                               @RequestParam("telephone") String telephoneNumber, @RequestParam("city") String cityName,
                               @RequestParam("username") String userName) {
        if (driverService.findDriverByTelephoneNumber(telephoneNumber)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
        DriverAdminDto driverToUpdate = new DriverAdminDto();
        driverToUpdate.setName(name);
        driverToUpdate.setSurname(surname);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverToUpdate.setCityName(cityName);
        driverToUpdate.setUserName(userName);
        driverService.add(driverToUpdate);

        return "redirect:/drivers/info";
    }

    @GetMapping("/edit_driver")
    public String editDriver(@RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("surname") String surname,
                             @RequestParam("telephoneNumber") String telephoneNumber, @RequestParam("city") String cityName) {
        if (driverService.checkEditedTelephoneNumber(telephoneNumber, id)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
        DriverAdminDto driverToUpdate = new DriverAdminDto();
        driverToUpdate.setId(id);
        driverToUpdate.setName(name);
        driverToUpdate.setSurname(surname);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverToUpdate.setCityName(cityName);
        driverService.update(driverToUpdate);

        return "redirect:/drivers/info";
    }
}
