package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;

import java.util.List;

public interface TruckService {

    List<Truck> getListOfTrucks();

    void deleteById(Long id);

    void update(Truck truck);

    Truck findById(Long id);

    boolean findByNumber(String number);

    void add(String number, int crew_cize, int capacity, TruckState state, Long cityId);
}
