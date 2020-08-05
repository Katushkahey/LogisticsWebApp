package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.Truck;

import java.util.List;

public interface TruckDao extends GenericDao<Truck> {

    void add(Truck truck);

    List<Truck> getAll();

    List<Truck> getAllUsable();

    Truck getByNumber(String number);

    void update(Truck truck);

    void remove(Truck truck);
}
