package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverDao extends GenericDao<Driver> {

    Driver findById(Long id);

    Driver findByUser(User user);

    List<Driver> findAll();

    List<Driver> findAllDriversForCurrentOrder(Order order);

}
