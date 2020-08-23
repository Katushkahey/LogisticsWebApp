package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    Order findById(Long id);

    List<Order> findCompetedOrders();

    List<Order> findUnassignedOrders();

    List<Order> findWaitingOrders();

    List<Order> findOrdersInProgress();

    Order findByNumber(String number);

    Order findByTruckId(Long truckId);

}
