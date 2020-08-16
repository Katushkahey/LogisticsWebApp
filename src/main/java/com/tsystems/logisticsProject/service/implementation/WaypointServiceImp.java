package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.service.CargoService;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaypointServiceImp implements WaypointService {

    @Autowired
    WaypointDao waypointDao;

    @Autowired
    CityService cityService;

    @Autowired
    OrderService orderService;

    @Autowired
    CargoService cargoService;

    @Transactional
    public Waypoint findById(Long id) {
        return waypointDao.findById(id);
    }

    @Transactional
    public void update(Waypoint waypoint) {
        waypointDao.update(waypoint);
    }

    @Transactional
    public void makeCompletedById(Long id) {
        Waypoint completedWaypoint = findById(id);
        completedWaypoint.setStatus(WaypointStatus.DONE);
        update(completedWaypoint);
    }

    @Transactional
    public List<Waypoint> getListOfWaypointsByOrderId(Long orderId) {
        return waypointDao.getListOfWaypointsByOrderId(orderId);
    }

    @Transactional
    public void editWaypoint(Long waypointId, String cargoName, double cargoWeight, String cityName) {
        Waypoint waypointToUpdate = waypointDao.findById(waypointId);
        waypointToUpdate.getCargo().setName(cargoName);
        waypointToUpdate.getCargo().setWeight(cargoWeight);
        waypointToUpdate.setCity(cityService.findByCityName(cityName));
        waypointDao.update(waypointToUpdate);
    }

    @Transactional
    public boolean deleteWaypoint(Long orderId, Long waypointId) {
        Order orderToUpdate = orderService.findById(orderId);
        List<Waypoint> listOfWaypoint = orderService.findWaypointsForCurrentOrderById(orderId);
        if (listOfWaypoint.size() == 2) {
            orderService.deleteById(orderId);
            return true;
        }
        Waypoint waypointToDelete = waypointDao.findById(waypointId);
        Cargo cargo = waypointToDelete.getCargo();
        orderToUpdate.getCargoes().remove(cargo);
        orderService.update(orderToUpdate);
        return false;
    }
}
