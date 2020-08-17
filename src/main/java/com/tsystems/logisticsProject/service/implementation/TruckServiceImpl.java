package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private CityService cityService;
    private TruckDao truckDao;

    @Autowired
    public TruckServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setDependencies(CityService cityService, TruckDao truckDao) {
        this.truckDao = truckDao;
        this.cityService = cityService;
    }

    @Transactional
    public List<Truck> getListOfTrucks() {
        return truckDao.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        truckDao.delete(truckDao.findById(id));
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
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
        return truckDao.findByNumber(number);
    }

    @Transactional
    public List<Truck> findAll() {
        return truckDao.findAll();
    }

    @Transactional
    public void add(Truck truck) {
        truckDao.add(truck);
    }

    @Transactional
    public void add(String number, int crew_cize, double capacity, TruckState state, String cityName) {
        Truck newTruck = new Truck();
        newTruck.setNumber(number);
        newTruck.setCrewSize(crew_cize);
        newTruck.setCapacity(capacity);
        newTruck.setTruckState(state);
        newTruck.setCurrentCity(cityService.findByCityName(cityName));
        add(newTruck);
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

}
