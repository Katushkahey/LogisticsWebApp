package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private Set<Cargo> cargoes = new HashSet<>();
    private Map<Order, List<Driver>> mapOfDriversForCompletedOders = new HashMap<>();
    private Map<Order, List<Driver>> mapOfDriversForUnassignedOders = new HashMap<>();
    private Map<Order, List<Driver>> mapOfDriversForWaitingOrders = new HashMap<>();
    private Map<Order, List<Driver>> mapOfDriversForOrdersInProgress = new HashMap<>();

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private DriverService driverService;

    @Autowired
    private WaypointService waypointService;

    @Transactional
    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return waypointService.getListOfWaypointsByOrderId(id);
    }

    @Transactional
    public HashMap<Order, Double> findCompletedOrders() {
        HashMap<Order, Double> completedOrderHashMap = new HashMap<>();
        List<Order> completedOrders = orderDao.findCompetedOrders();
        for (Order order : completedOrders) {
            completedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForCompletedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return completedOrderHashMap;
    }

    @Transactional
    public HashMap<Order, Double> findUnassignedOrders() {
        HashMap<Order, Double> unassignedOrderHashMap = new HashMap<>();
        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
        for (Order order : unassignedOrders) {
            unassignedOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForUnassignedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return unassignedOrderHashMap;
    }

    @Transactional
    public HashMap<Order, Double> findWaitingOrders() {
        HashMap<Order, Double> waytingOrderHashMap = new HashMap<>();
        List<Order> waytingOrders = orderDao.findWaitingOrders();
        for (Order order : waytingOrders) {
            waytingOrderHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForWaitingOrders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return waytingOrderHashMap;

    }

    @Transactional
    public HashMap<Order, Double> findOrdersInProgress() {
        HashMap<Order, Double> ordersInProgressHashMap = new HashMap<>();
        List<Order> ordersInProgress = orderDao.findOrdersInProgress();
        for (Order order : ordersInProgress) {
            ordersInProgressHashMap.put(order, getMaxWeightDuringTheRouteOfCurrentOrderById(order.getId()));
            mapOfDriversForOrdersInProgress.put(order, driverService.getParnersForCurrentOrder(order.getId()));
        }
        return ordersInProgressHashMap;
    }

    @Transactional
    public HashMap<Order, List<Waypoint>> findListOfWaypointsForCompletedOrders() {
        List<Order> completedOrders = orderDao.findCompetedOrders();
        return findListOfWaypointsForOrders(completedOrders);
    }

    @Transactional
    public HashMap<Order, List<Waypoint>> findListOfWaypointsForWaytingOrders() {
        List<Order> waitingOrders = orderDao.findWaitingOrders();
        return findListOfWaypointsForOrders(waitingOrders);
    }

    @Transactional
    public HashMap<Order, List<Waypoint>> findListOfWaypointsForOrdersInProgress() {
        List<Order> ordersInProgress = orderDao.findOrdersInProgress();
        return findListOfWaypointsForOrders(ordersInProgress);
    }

    @Transactional
    public HashMap<Order, List<Waypoint>> findListOfWaypointsForUnassignedOrders() {
        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
        return findListOfWaypointsForOrders(unassignedOrders);
    }

    @Transactional
    public HashMap<Order, List<Waypoint>> findListOfWaypointsForOrders(List<Order> listOfOrders) {
        HashMap<Order, List<Waypoint>> mapOfWaypoints = new HashMap<>();
        for (Order order : listOfOrders) {
            mapOfWaypoints.put(order, findWaypointsForCurrentOrderById(order.getId()));
        }
        return mapOfWaypoints;
    }

    @Transactional
    public List<Cargo> getListOfCargoesForCurrentOrderById(Long id) {
        return findById(id).getCargoes();
    }

    @Transactional
    public double getMaxWeightDuringTheRouteOfCurrentOrderById(Long id) {
        double maxWeight = 0;
        double totalWeight = 0;
        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
        for (Waypoint waypoint : waypoints) {
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
    public Map<Order, List<Driver>> getMapOfDriversForWaitingOrders() {
        return mapOfDriversForWaitingOrders;
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
    public Map<Order, List<Driver>> getMapOfDriversForOrdersInProgress() {
        return mapOfDriversForOrdersInProgress;
    }

    @Transactional
    public Order findById(Long id) {
        return orderDao.findById(id);
    }

    @Transactional
    public void update(Order order) {
        orderDao.update(order);
    }

    @Transactional
    public void startOrder(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderDao.update(order);
    }

    @Transactional
    public void add(Order order) {
        orderDao.add(order);
    }

    @Transactional
    public void assign(Long orderId, Truck truck, List<Driver> listOfDrivers) {
        Order orderToUpdate = findById(orderId);
        orderToUpdate.setOrderTruck(truck);
        orderToUpdate.setDrivers(listOfDrivers);
        orderToUpdate.setStatus(OrderStatus.WAITING);
        orderDao.update(orderToUpdate);
    }

}
