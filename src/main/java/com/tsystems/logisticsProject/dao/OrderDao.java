package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    void add(Order order);

    Order findById(Long id);

    void update(Order order);

    void delete(Order order);

    List<Order> findCompetedOrders();

    List<Order> findUnassignedOrders();

    List<Order> findWaitingOrders();

    List<Order> findOrdersInProgress();

}
