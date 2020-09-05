package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.InfoboardService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private InfoboardService infoboardService;
    private CityService cityService;
    private TruckDao truckDao;

    @Autowired
    public TruckServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setDependencies(CityService cityService, InfoboardService infoboardService, TruckDao truckDao) {
        this.truckDao = truckDao;
        this.infoboardService = infoboardService;
        this.cityService = cityService;
    }

    @Transactional
    public List<Truck> getListOfTrucks() {
        return truckDao.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        truckDao.delete(truckDao.findById(id));
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public void update(Truck truck) {
        truckDao.update(truck);
    }

    @Transactional
    public Truck findById(Long id) {
        return truckDao.findById(id);
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
    public List<Truck> findAll() {
        return truckDao.findAll();
    }

    @Transactional
    public void add(String number, int crew_cize, double capacity, TruckState state, String cityName) {
        Truck newTruck = new Truck();
        newTruck.setNumber(number);
        newTruck.setCrewSize(crew_cize);
        newTruck.setCapacity(capacity);
        newTruck.setTruckState(state);
        newTruck.setCurrentCity(cityService.findByCityName(cityName));
        truckDao.add(newTruck);
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public boolean checkEditedNumber(String number, Long id) {
        return truckDao.checkEditedNumber(number, id);
    }

    @Transactional
    public void update(Long id, String number, double capacity, int crew, TruckState truckState, String cityName) {
        Truck truckToUpdate = findById(id);
        truckToUpdate.setNumber(number);
        truckToUpdate.setCapacity(capacity);
        truckToUpdate.setCrewSize(crew);
        truckToUpdate.setTruckState(truckState);
        truckToUpdate.setCurrentCity(cityService.findByCityName(cityName));
        update(truckToUpdate);
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public double getMaxCapacity() {
        List<Truck> listOfTruck = findAll();
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
    public LinkedHashMap<String, Integer> getTrucksInfo() {
        LinkedHashMap<String, Integer> mapOfTrucks = new LinkedHashMap<>();
        mapOfTrucks.put("Broken", truckDao.getBrokenTrucks().size());
        mapOfTrucks.put("Available", truckDao.getAvailableTrucks().size());
        mapOfTrucks.put("Employed", truckDao.getEmployedTrucks().size());
        System.out.println("Broken " + mapOfTrucks.get("Broken"));
        System.out.println("Available " + mapOfTrucks.get("Available"));
        System.out.println("Employed " + mapOfTrucks.get("Employed"));
        return mapOfTrucks;
    }

}
