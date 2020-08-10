package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.TruckDaoImpl;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.abstraction.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TruckServiceImpl implements TruckService {

    private TruckDaoImpl truckDaoImpl;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TruckServiceImpl(ApplicationEventPublisher applicationEventPublisher, TruckDaoImpl truckDaoImpl) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.truckDaoImpl = truckDaoImpl;
    }

    public List<Truck> getListOfTrucks() {
        return truckDaoImpl.findAll();
    }

    public void deleteById(Long id) {
        truckDaoImpl.delete(truckDaoImpl.findById(id));
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    public void update(Truck truck) {
        truckDaoImpl.update(truck);
    }
}
