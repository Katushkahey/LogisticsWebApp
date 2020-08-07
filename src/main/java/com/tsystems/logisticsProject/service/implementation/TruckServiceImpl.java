package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.TruckDaoImpl;
import com.tsystems.logisticsProject.entity.Truck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckServiceImpl {

    @Autowired
    private TruckDaoImpl truckDaoImpl;

    public List<Truck> getListOfTrucks() {
        return truckDaoImpl.findAll();
    }

    public void deleteById(Long id) {
        truckDaoImpl.delete(truckDaoImpl.findById(id));
    }
}
