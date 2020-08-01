package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.Order;

import java.util.List;

public interface OrderDao {

    void add(Order order);

    List<Order> getAllOrders();

    Order getById(Long id);

    void update(Order order);

    void remove(Order order);
}
