package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.exception.checked.NotUniqueTruckNumberException;
import com.tsystems.logisticsProject.mapper.TruckMapper;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private ApplicationEventPublisher applicationEventPublisher;
    private TruckMapper truckMapper;
    private TruckDao truckDao;

    @Autowired
    public void setDependencies(TruckDao truckDao, TruckMapper truckMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.truckDao = truckDao;
        this.truckMapper = truckMapper;
        this.applicationEventPublisher = applicationEventPublisher;
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
    public void add(TruckDto truckDto) throws NotUniqueTruckNumberException {
        try {
            truckDao.findByNumber(truckDto.getNumber());
            throw new NotUniqueTruckNumberException(truckDto.getNumber());
        } catch (NoResultException e) {
            truckDao.add(truckMapper.toEntity(truckDto));
            applicationEventPublisher.publishEvent(new UpdateEvent());
        }
    }

    @Transactional
    public void update(TruckDto truckDto) throws NotUniqueTruckNumberException {
        try {
            Truck truck = truckDao.findByNumber(truckDto.getNumber());
            if (truck.getId() == truckDto.getId()) {
                truckDao.update(truckMapper.toEntity(truckDto));
                applicationEventPublisher.publishEvent(new UpdateEvent());
            } else {
                throw new NotUniqueTruckNumberException(truckDto.getNumber());
            }
        } catch (NoResultException e) {
            truckDao.update(truckMapper.toEntity(truckDto));
            applicationEventPublisher.publishEvent(new UpdateEvent());
        }
    }

    @Transactional
    public double getMaxCapacity() {
        List<Truck> listOfTruck = getListOfTrucks();
        return calculateMaxCapacity(listOfTruck);
    }

    public double calculateMaxCapacity(List<Truck> listOfTruck) {
        double maxCapacity = 0;
        for (Truck truck : listOfTruck) {
            double capacity = truck.getCapacity();
            if (capacity > maxCapacity) {
                maxCapacity = capacity;
            }
        }
        return maxCapacity * 1000;
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
