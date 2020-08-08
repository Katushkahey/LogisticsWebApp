package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.OrderDaoImpl;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl {

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
}
