package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private Set<Cargo> cargoes = new HashSet<>();
    private Map<Order, List<Driver>> mapOfDriversForCompletedOders = new HashMap<>();
    private Map<Order, List<Driver>> mapOfDriversForUnassignedOders = new HashMap<>();
    private Map<Order, List<Driver>> mapOfDriversForAssignedOders = new HashMap<>();

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private DriverService driverService;

    @Transactional
    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return orderDao.findById(id).getWaypoints();
    }

    @Transactional
    public HashMap<Order, Double> findCompletedOrders() {
        HashMap<Order, Double> completedOrderHashMap = new HashMap<>();
        List<Order> completedOrders = orderDao.findCompetedOrders();
        for (Order order: completedOrders) {
            completedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForCompletedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return completedOrderHashMap;
    }

    @Transactional
    public HashMap<Order, Double> findUnassignedOrders() {
        HashMap<Order, Double> unassignedOrderHashMap = new HashMap<>();
        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
        for (Order order: unassignedOrders) {
            unassignedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForUnassignedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return unassignedOrderHashMap;
    }

    @Transactional
    public HashMap<Order, Double> findAssignedOrders() {
        HashMap<Order, Double> assignedOrderHashMap = new HashMap<>();
        List<Order> assignedOrders = orderDao.findAssignedOrders();
        for (Order order: assignedOrders) {
            assignedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForAssignedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return assignedOrderHashMap;

    }

    @Transactional
    public Set<Cargo> getListOfCargoesForCurrentOrderById(Long id) {
        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
        for (Waypoint waypoint: waypoints) {
            cargoes.add(waypoint.getCargo());
        }
        return cargoes;
    }

    @Transactional
    public double getMaxWeightDuringTheRouteOfCurrentOrderById(Long id) {
        double maxWeight = 0;
        double totalWeight = 0;
        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
        for (Waypoint waypoint: waypoints) {
            if (waypoint.getAction().equals(Action.LOADING)) {
                totalWeight += waypoint.getCargo().getWeight();
                if (maxWeight < totalWeight) {
                    maxWeight = totalWeight;
                }
            } else {
                totalWeight -= waypoint.getCargo().getWeight();
            }
        }
        return maxWeight;
    }

    @Transactional
    public void deleteById(Long id) {
        orderDao.delete(orderDao.findById(id));
    }

    @Transactional
    public Map<Order, List<Driver>> getMapOfDriversForAssignedOrders() {
        return mapOfDriversForAssignedOders;
    }

    @Transactional
    public Map<Order, List<Driver>> getMapOfDriversForUnassignedOrders() {
        return mapOfDriversForUnassignedOders;
    }

    @Transactional
    public Map<Order, List<Driver>> getMapOfDriversForCompletedOrders() {
        return mapOfDriversForCompletedOders;
    }

    @Transactional
    public Order findById(Long id) {
        return orderDao.findById(id);
    }


}
