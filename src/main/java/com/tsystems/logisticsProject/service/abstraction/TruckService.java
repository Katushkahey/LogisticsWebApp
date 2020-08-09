package com.tsystems.logisticsProject.service.abstraction;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;

import java.util.List;

public interface TruckService {

    public List<Truck> getListOfTrucks();

    public void deleteById(Long id);
}
