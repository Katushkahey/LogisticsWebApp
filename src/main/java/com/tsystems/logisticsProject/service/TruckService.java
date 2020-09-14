package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.exception.checked.NotUniqueTruckNumberException;

import java.util.LinkedHashMap;
import java.util.List;

public interface TruckService {

    List<Truck> getListOfTrucks();

    void deleteById(Long id);

    void add(TruckDto truckDto) throws NotUniqueTruckNumberException;

    void update(TruckDto truckDto) throws NotUniqueTruckNumberException;

    double getMaxCapacity();

    List<Truck> findTrucksForOrder(double maxOneTimeWeight);

    LinkedHashMap<String, Long> getTrucksInfo();

}
