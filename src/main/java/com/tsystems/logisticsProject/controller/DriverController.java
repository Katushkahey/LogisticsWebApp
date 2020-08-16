package com.tsystems.logisticsProject.controller;

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
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private CityService cityService;

    @GetMapping("/info")
    public String driversInfo(Model model) {
        model.addAttribute("listOfDrivers", driverService.getListOfDrivers());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "drivers_page";
    }

    @GetMapping("/delete_driver/{id}")
    public String deleteDriver(@PathVariable("id") Long id, Model model) {
        driverService.deleteById(id);
        model.addAttribute("listOfDrivers", driverService.getListOfDrivers());

        return "redirect:/drivers/info";
    }

    //могу ли я создать здесь сущность User, что бы не гонять 1 и тот же довольно сложный метод дважды?
    @GetMapping("/create_driver")
    public String createDriver(@RequestParam("name") String name, @RequestParam("surname") String surname,
                               @RequestParam("telephone") String telephoneNumber, @RequestParam("city") String cityName,
                               @RequestParam("userName") String userName) {
        if (driverService.findDriverByTelephoneNumber(telephoneNumber)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
// else if (driverService.returnUserToCreateDriver(userName) == null) {
//            return "error";  //данное имя пользователя уже занято
//        }
        driverService.add(name, surname, telephoneNumber, cityName, driverService.returnUserToCreateDriver(userName));
//        альтернативный вариант с User:

//        } else {
//            User user = driverService.returnUserToCreateDriver(userName);
//            if (user == null) {
//                return "error" //данное имя пользовотеля уже занято
//            } else {
//                driverService.add(name, surname, telephoneNumber, cityName, user);
//            }
//        }
        return "redirect:/drivers/info";
    }

    @GetMapping("/edit_driver")
    public String editDriver(@RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("surname") String surname,
                             @RequestParam("telephoneNumber") String telephoneNumber, @RequestParam("city") String cityName) {
        if (driverService.checkEditedTelephoneNumber(telephoneNumber, id)) {
            return "error"; //водитель с таким номером телефона уже существует
        }
        driverService.update(id, name, surname, telephoneNumber, cityName);

        return "redirect:/drivers/info";
    }
}
