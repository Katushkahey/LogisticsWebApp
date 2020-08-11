package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverDao extends GenericDao<Driver> {

    Driver findById(Long id);

}
