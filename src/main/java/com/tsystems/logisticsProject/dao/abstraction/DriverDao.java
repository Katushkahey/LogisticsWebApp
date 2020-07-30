package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.Driver;

import java.util.List;

public interface DriverDao {

    void add(Driver driver);

    List<Driver> getAllDrivers();

    Driver getById(Long id);

    void update(Driver driver);

    void remove(Driver driver);
}
