package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverDao extends GenericDao<Driver> {

    Driver findById(Long id);

    Driver findByUsername(String username);

    List<Driver> getAll();

    List<Driver> findAllAvailable(City currentCity, LocalDateTime requiredTime);

}
