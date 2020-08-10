package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.OrderDaoImpl;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.service.abstraction.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private Set<Cargo> cargoes = new HashSet<>();

    @Autowired
    OrderDaoImpl orderDaoImpl;

    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return orderDaoImpl.findById(id).getWaypoints();
    }

    public HashMap<Order, Double> findCompletedOrders() {
        HashMap<Order, Double> completedOrderHashMap = new HashMap<>();
        List<Order> completedOrders = orderDaoImpl.findCompetedOrders();
        for (Order order: completedOrders) {
            completedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
        }
        return completedOrderHashMap;
    }

    public HashMap<Order, Double> findUnassignedOrders() {
        HashMap<Order, Double> unassignedOrderHashMap = new HashMap<>();
        List<Order> unassignedOrders = orderDaoImpl.findUnassignedOrders();
        for (Order order: unassignedOrders) {
            unassignedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
        }
        return unassignedOrderHashMap;
    }

    public HashMap<Order, Double> findAssignedOrders() {
        HashMap<Order, Double> assignedOrderHashMap = new HashMap<>();
        List<Order> assignedOrders = orderDaoImpl.findAssignedOrders();
        for (Order order: assignedOrders) {
            assignedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
        }
        return assignedOrderHashMap;

    }

    public Set<Cargo> getListOfCargoesForCurrentOrderById(Long id) {
        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
        for (Waypoint waypoint: waypoints) {
            cargoes.add(waypoint.getCargo());
        }
        return cargoes;
    }

    private double getMaxWeightDuringTheRouteOfCurrentOrderById(Long id) {
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

    public void deleteById(Long id) {
        orderDaoImpl.delete(orderDaoImpl.findById(id));
    }

}
