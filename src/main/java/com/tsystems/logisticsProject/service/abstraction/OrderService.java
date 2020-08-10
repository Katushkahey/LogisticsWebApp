package com.tsystems.logisticsProject.service.abstraction;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface OrderService {

    public List<Waypoint> findWaypointsForCurrentOrderById(Long id);

    public HashMap<Order, Double> findCompletedOrders();

    public HashMap<Order, Double> findUnassignedOrders();

    public HashMap<Order, Double> findAssignedOrders();

    public Set<Cargo> getListOfCargoesForCurrentOrderById(Long id);

    public void deleteById(Long id);
}
