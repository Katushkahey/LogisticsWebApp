package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Truck;

public interface TruckDao extends GenericDao<Truck> {

    void add(Truck truck);

    boolean findByNumber(String number);

    void update(Truck truck);

    void delete(Truck truck);

}
