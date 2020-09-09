package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.mapper.TruckMapper;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private TruckMapper truckMapper;
    private TruckDao truckDao;

    @Autowired
    public TruckServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setDependencies(TruckDao truckDao, TruckMapper truckMapper) {
        this.truckDao = truckDao;
        this.truckMapper = truckMapper;
    }

    @Transactional
    public List<Truck> getListOfTrucks() {
        return truckDao.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        truckDao.delete(truckDao.findById(id));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public boolean findByNumber(String number) {
        if(truckDao.findByNumber(number) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public void add(TruckDto truckDto) {
        truckDao.add(truckMapper.toEntity(truckDto));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public boolean checkEditedNumber(String number, Long id) {
        return truckDao.checkEditedNumber(number, id);
    }

    @Transactional
    public void update(TruckDto truckDto) {
        truckDao.update(truckMapper.toEntity(truckDto));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public double getMaxCapacity() {
        List<Truck> listOfTruck = getListOfTrucks();
        double maxCapacity = 0;
        for (Truck truck : listOfTruck) {
            double capacity = truck.getCapacity();
            if (capacity > maxCapacity) {
                maxCapacity = capacity;
            }
        }
        return maxCapacity;
    }

    @Transactional
    public List<Truck> findTrucksForOrder(double maxOneTimeWeight) {
        return truckDao.findTrucksForOrder(maxOneTimeWeight);
    }

    @Transactional
    public LinkedHashMap<String, Long> getTrucksInfo() {
        LinkedHashMap<String, Long> mapOfTrucks = new LinkedHashMap<>();
        mapOfTrucks.put("Broken", truckDao.getBrokenTrucks());
        mapOfTrucks.put("Available", truckDao.getAvailableTrucks());
        mapOfTrucks.put("Employed", truckDao.getEmployedTrucks());
        return mapOfTrucks;
    }

}
