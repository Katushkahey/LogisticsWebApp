package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Truck;

import java.util.List;

public interface TruckDao extends GenericDao<Truck> {

    Truck findByNumber(String number);

    List<Truck> findAll();

    Truck findById(Long id);

    List<Truck> findTrucksForOrder(double maxOneTimeWeight);

    Long getBrokenTrucks();

    Long getAvailableTrucks();

    Long getEmployedTrucks();

}
