package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {

    List<Waypoint> findWaypointsForCurrentOrderById(Long id);

    HashMap<Order, Double> findCompletedOrders();

    HashMap<Order, Double> findUnassignedOrders();

    HashMap<Order, Double> findWaitingOrders();

    HashMap<Order, Double> findOrdersInProgress();

    Order findById(Long id);

    List<Cargo> getListOfCargoesForCurrentOrderById(Long id);

    double getMaxWeightDuringTheRouteOfCurrentOrderById(Long id);

    void deleteById(Long id);

    Map<Order, List<Driver>> getMapOfDriversForWaitingOrders();

    Map<Order, List<Driver>> getMapOfDriversForUnassignedOrders();

    Map<Order, List<Driver>> getMapOfDriversForCompletedOrders();

    Map<Order, List<Driver>> getMapOfDriversForOrdersInProgress();

    void add(Order order);

    void update(Order order);

    void startOrder(Long id);

    HashMap<Order, List<Waypoint>> findListOfWaypointsForCompletedOrders();

    HashMap<Order, List<Waypoint>> findListOfWaypointsForWaytingOrders();

    HashMap<Order, List<Waypoint>> findListOfWaypointsForOrdersInProgress();

    HashMap<Order, List<Waypoint>> findListOfWaypointsForUnassignedOrders();

    void assign(Long orderId, Truck truck, List<Driver> listOfDrivers);

}
