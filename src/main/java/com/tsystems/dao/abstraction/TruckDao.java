package com.tsystems.dao.abstraction;

import com.tsystems.entity.Truck;

import java.util.List;

public interface TruckDao {

    void add(Truck truck);

    List<Truck> getAllTrucks();

    Truck getById(Long id);

    void update(Truck truck);

    void remove(Truck truck);
}
