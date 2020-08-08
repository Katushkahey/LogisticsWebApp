package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.OrderDaoImpl;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl {

    private Set<Cargo> cargoes = new HashSet<>();

    @Autowired
    OrderDaoImpl orderDaoImpl;

    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return orderDaoImpl.findById(id).getWaypoints();
    }

    public List<Order> findCompletedOrders() {
        return orderDaoImpl.findCompetedOrders();
    }

    public List<Order> findUnassignedOrders() {
        return orderDaoImpl.findUnassignedOrders();
    }

    public List<Order> findAssignedOrders() {
        return orderDaoImpl.findAssignedOrders();
    }

    public Set<Cargo> getCargoesForCurrentOrderById(Long id) {
        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
        for (Waypoint waypoint: waypoints) {
            cargoes.add(waypoint.getCargo());
        }
        return cargoes;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        Iterator<Cargo> iterator = cargoes.iterator();
        while (iterator.hasNext()) {
            totalWeight += iterator.next().getWeight();
        }
        return totalWeight;
    }

}
