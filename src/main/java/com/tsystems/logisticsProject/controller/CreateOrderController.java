package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.util.RawOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/create_order")
public class CreateOrderController {

    @Autowired
    private RawOrder rawOrder;

    @Autowired
    private TruckService truckService;

    @Autowired
    private CityService cityService;

    @GetMapping("")
    public String returnPageToCreateOrder(Model model) {
        model.addAttribute("listOfCargoes", rawOrder.getListOfCargoes());
        model.addAttribute("mapOfCargoes", rawOrder.getMapOfCargoes());
        model.addAttribute("listOfWaypoints", rawOrder.getListOfWaypoints());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "order_create_page";
    }

    @GetMapping("/add_cargo")
    public String addCargo(@RequestParam("name") String name, @RequestParam("weight") Double weight) {
        rawOrder.addNewCargo(name, weight);
        return "redirect:/create_order";
    }

    @GetMapping("/delete_cargo/{id}")
    public String deleteCarggo(@PathVariable("id") Long id) {
        rawOrder.deleteCargoById(id);
        return "redirect:/create_order";
    }

    @GetMapping("/edit_cargo")
    public String editCargo(@RequestParam("id") Long id, @RequestParam("name") String name,
                            @RequestParam("weight") double weight) {
        rawOrder.editCargo(id, name, weight);
        return "redirect:/create_order";
    }

    @GetMapping("/save_cargoes")
    public String saveCargoes() {
        rawOrder.saveCargoes();
        return "redirect:/create_order";
    }

    @GetMapping("/add_waypoint")
    public String addWaypoint(@RequestParam("cargoId") Long cargoId, @RequestParam("cityName") String cityName) {
        rawOrder.addWaypoint(cargoId, cityName);
        return "redirect:/create_order";
    }

    @GetMapping("delete_waypoint/{id}")
    public String deleteWaypoint(@PathVariable("id") Long id) {
        rawOrder.deleteWaypointById(id);
        return "redirect:/create_order";
    }

    @GetMapping("save_order")
    public String saveOrder() {
        rawOrder.saveOrder();
        return "redirect:/order/info";
    }

    @GetMapping("clear_all")
    public String clearAll() {
        rawOrder.clearAll();
        return "redirect:/order/info";
    }

}
