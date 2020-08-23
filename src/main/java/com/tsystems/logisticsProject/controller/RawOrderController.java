package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.service.impl.RawOrderSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/create_order")
public class RawOrderController {

    private RawOrderSessionService rawOrderService;
    private TruckService truckService;
    private CityService cityService;

    @Autowired
    public void setDependencies(RawOrderSessionService rawOrderService, TruckService truckService,
                                CityService cityService) {
        this.rawOrderService = rawOrderService;
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @GetMapping("")
    public String returnPageToCreateOrder(Model model) {
        model.addAttribute("listOfCargoes", rawOrderService.getListOfCargoes());
        model.addAttribute("mapOfCargoes", rawOrderService.getMapOfCargoes());
        model.addAttribute("listOfWaypoints", rawOrderService.getListOfWaypoints());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "order_create_page";
    }

    @GetMapping("/add_cargo")
    public String addCargo(@RequestParam("name") String name, @RequestParam("cargoWeight") Double weight) {
        rawOrderService.addNewCargo(name, weight);
        return "redirect:/create_order";
    }

    @GetMapping("/delete_cargo/{id}")
    public String deleteCargo(@PathVariable("id") Long id) {
        rawOrderService.deleteCargoById(id);
        return "redirect:/create_order";
    }

    @GetMapping("/edit_cargo")
    public String editCargo(@RequestParam("id") Long id, @RequestParam("name") String name,
                            @RequestParam("cargoWeight") double weight) {
        rawOrderService.editCargo(id, name, weight);
        return "redirect:/create_order";
    }

    @GetMapping("/save_cargoes")
    public String saveCargoes() {
        rawOrderService.saveCargoes();
        return "redirect:/create_order";
    }

    @GetMapping("/add_waypoint")
    public String addWaypoint(@RequestParam("cargoId") Long cargoId, @RequestParam("cityName") String cityName) {
        rawOrderService.addWaypoint(cargoId, cityName);
        return "redirect:/create_order";
    }

    @GetMapping("/edit_waypoint")
    public String editWaypoint(@RequestParam("id") Long id, @RequestParam("cityName") String cityName) {
        rawOrderService.editWaypoint(id, cityName);
        return "redirect:/create_order";
    }

    @GetMapping("delete_waypoint/{id}")
    public String deleteWaypoint(@PathVariable("id") Long id) {
        rawOrderService.deleteWaypointById(id);
        return "redirect:/create_order";
    }

    @GetMapping("save_order")
    public String saveOrder(@RequestParam("orderNumber") String number, Model model) {
        if (!rawOrderService.checkMaxWeightOfOrder()) {
            model.addAttribute("error", true);
            return "error";
        }
        rawOrderService.saveOrder(number);
        return "redirect:/order/info";
    }

    @GetMapping("clear_all")
    public String clearAll() {
        rawOrderService.clearAll();
        return "redirect:/order/info";
    }

}
