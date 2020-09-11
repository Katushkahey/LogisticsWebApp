package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
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
        model.addAttribute("listOfCargoes", rawOrderService.getListOfAllCargoes());
        model.addAttribute("mapOfCargoes", rawOrderService.getListOfCargoesToUnload());
        model.addAttribute("listOfWaypoints", rawOrderService.getListOfWaypoints());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "order_create_page";
    }

//    @GetMapping("/add_cargo")
//    public String addCargo(@RequestParam("name") String name, @RequestParam("cargoWeight") Double weight) {
//        rawOrderService.addNewCargo(name, weight);
//        return "redirect:/create_order";
//    }

//    @GetMapping("/delete_cargo/{id}")
//    public String deleteCargo(@PathVariable("id") Long id) {
//        rawOrderService.deleteCargoById(id);
//        return "redirect:/create_order";
//    }

//    @GetMapping("/edit_cargo")
//    public String editCargo(@RequestParam("id") Long id, @RequestParam("name") String name,
//                            @RequestParam("cargoWeight") double weight) {
//        rawOrderService.editCargo(id, name, weight);
//        return "redirect:/create_order";
//    }

//    @GetMapping("/save_cargoes")
//    public String saveCargoes() {
//        rawOrderService.saveCargoes();
//        return "redirect:/create_order";
//    }

    @GetMapping("/add_loading_waypoint")
    public String addLoadingWaypoint(@RequestParam("cargoName") String cargoName, @RequestParam("cityWeight")
            Double cargoWeight, @RequestParam("cityName") String cityName) {
        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setCargoName(cargoName);
        waypointDto.setCargoWeight(cargoWeight);
        waypointDto.setCityName(cityName);
        waypointDto.setStatus(WaypointStatus.TODO.toString());
        waypointDto.setAction(Action.LOADING.toString());
        rawOrderService.addLoadingWaypoint(waypointDto);
        return "redirect:/create_order";
    }


    @GetMapping("/add_unloading_waypoint")
    public String addUnloadingWaypoint(@RequestParam("cargoNumber") String cargoNumber,
                                       @RequestParam("cargoName") String cargoName,
                                       @RequestParam("cargoWeight") Double cargoWeight,
                                       @RequestParam("cityName") String cityName) {

        WaypointDto waypointDto = new WaypointDto();
        waypointDto.setCargoNumber(cargoNumber);
        waypointDto.setCargoName(cargoName);
        waypointDto.setCargoWeight(cargoWeight);
        waypointDto.setStatus(WaypointStatus.TODO.toString());
        waypointDto.setAction(Action.UNLOADING.toString());
        rawOrderService.addUnloadingWaypoint(waypointDto);

        return "redirect:/create_order";
    }

    @GetMapping("/edit_waypoint")
    public String editWaypoint(@RequestParam("id") Long id, @RequestParam("cityName") String cityName) {
        WaypointDto waypointDto = new WaypointDto();
        rawOrderService.editWaypoint(waypointDto);
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
