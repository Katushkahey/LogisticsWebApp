package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

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

    Set<Cargo> getListOfCargoesForCurrentOrderById(Long id);

    void deleteById(Long id);

    Map<Order, List<Driver>> getMapOfDriversForWaitingOrders();

    Map<Order, List<Driver>> getMapOfDriversForUnassignedOrders();

    Map<Order, List<Driver>> getMapOfDriversForCompletedOrders();

    Map<Order, List<Driver>> getMapOfDriversForOrdersInProgress();

    void update(Order order);

}
