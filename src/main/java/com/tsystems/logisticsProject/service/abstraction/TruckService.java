package com.tsystems.logisticsProject.service.abstraction;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;

import java.util.List;

public interface TruckService {

    public List<Truck> getListOfTrucks();

    public void deleteById(Long id);

    public void update(Truck truck);

    public Truck findById(Long id);

    public boolean findByNumber(String number);

    public void add(String number, int crew_cize, int capacity, TruckState state, Long cityId);
}
