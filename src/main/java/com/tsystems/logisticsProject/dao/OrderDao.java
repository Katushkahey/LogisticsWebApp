package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    Order findById(Long id);

    List<Order> findCompetedOrders();

    List<Order> findUnassignedOrders();

    List<Order> findWaitingOrders();

    List<Order> findOrdersInProgress();

    Order findByNumber(String number);

    Order findByTruck(Truck truck);

    List<Order> getTopOrders(int number);

    Truck findTruckOfOrder(Long id);

}
