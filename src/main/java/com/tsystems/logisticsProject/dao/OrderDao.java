package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;

import java.util.List;

public interface OrderDao extends GenericDao<Truck> {

    void add(Order order);

    List<Order> getAll();

    Order getById(Long id);

    void update(Order order);

    void remove(Order order);
}
