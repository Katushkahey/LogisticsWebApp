package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Truck;

import java.util.LinkedHashMap;
import java.util.List;

public interface TruckService {

    List<Truck> getListOfTrucks();

    void deleteById(Long id);

    boolean findByNumber(String number);

    void add(TruckDto truckDto);

    void update(TruckDto truckDto);

    double getMaxCapacity();

    List<Truck> findTrucksForOrder(double maxOneTimeWeight);

    LinkedHashMap<String, Long> getTrucksInfo();

}
