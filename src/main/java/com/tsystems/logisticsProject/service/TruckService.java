package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;

import java.util.List;

public interface TruckService {

    List<Truck> getListOfTrucks();

    void deleteById(Long id);

    void update(Truck truck);

    Truck findById(Long id);

    boolean findByNumber(String number);

    void add(String number, int crew_cize, int capacity, TruckState state, String cityName);

    boolean checkEditedNumber(String number, Long id);

    void update(Long id, String nubmer, int capacity, int crew, TruckState truckState, String cityName);

}
