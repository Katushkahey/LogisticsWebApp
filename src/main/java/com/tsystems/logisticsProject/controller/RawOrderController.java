package com.tsystems.logisticsProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.exception.checked.TooLargeOrderTotalWeightException;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.service.impl.RawOrderSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/create_order")
public class RawOrderController {

    private ObjectMapper objectMapper;
    private RawOrderSessionService rawOrderService;
    private TruckService truckService;
    private CityService cityService;

    @Autowired
    public void setDependencies(RawOrderSessionService rawOrderService, TruckService truckService,
                                CityService cityService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rawOrderService = rawOrderService;
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @GetMapping
    public String returnPageToCreateOrder(Model model) {
        model.addAttribute("order", rawOrderService.getOrderDto());
        model.addAttribute("maxWeight", truckService.getMaxCapacity());
        model.addAttribute("listOfCities", cityService.getListOfCities());

        return "order_create_page";
    }

    @PostMapping(value = "/add_loading_waypoint", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addLoadingWaypoint(HttpServletRequest request) {
        try {
            NewOrderWaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), NewOrderWaypointDto.class);
            rawOrderService.addLoadingWaypoint(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @PostMapping(value = "/add_unloading_waypoint", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addUnloadingWaypoint(HttpServletRequest request) {
        try {
            NewOrderWaypointDto waypointDto = objectMapper.readValue(request.getInputStream(), NewOrderWaypointDto.class);
            rawOrderService.addUnloadingWaypoint(waypointDto);
            return "{\"success\":1}";
        } catch (Exception e) {  // autoCatch for TooLargeOrderTotalWeightException
            return "{\"error\":" + e.getMessage() + "}";
        }
    }

    @GetMapping("/edit_waypoint")
    public String editWaypoint(@RequestParam("id") Long id, @RequestParam("cityName") String cityName) {
        NewOrderWaypointDto waypointDto = new NewOrderWaypointDto();
        try {
            rawOrderService.editWaypoint(waypointDto);
        } catch (TooLargeOrderTotalWeightException e) {

        }
        return "redirect:/create_order";
    }

    @GetMapping("delete_waypoint/{id}")
    public String deleteWaypoint(@PathVariable("id") Long id) {
        rawOrderService.deleteWaypointById(id);
        return "redirect:/create_order";
    }

    @GetMapping("save_order")
    public String saveOrder() {
        rawOrderService.saveOrder();
        return "redirect:/order/info";
    }

    @GetMapping("clear_all")
    public String clearAll() {
        rawOrderService.clearAll();
        return "redirect:/order/info";
    }
}
