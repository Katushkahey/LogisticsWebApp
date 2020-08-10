package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.DriverDaoImpl;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.abstraction.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class DriverServiceImpl implements DriverService {

    private DriverDaoImpl driverDaoImpl;
    private UserServiceImpl userServiceImpl;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public DriverServiceImpl(ApplicationEventPublisher applicationEventPublisher, DriverDaoImpl driverDao, UserServiceImpl userService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.driverDaoImpl = driverDao;
        this.userServiceImpl = userService;
    }

    public Driver getDriverByPrincipalName(String name) {
        User userPrincipal = userServiceImpl.findByUsername(name);
        return driverDaoImpl.findByUser(userPrincipal);
    }

    private Order getCurrentOrderFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getCurrentOrder();
    }

    public List<Waypoint> getListOfWaypointsFromPrincipal(String name) {
        return getCurrentOrderFromPrincipal(name).getWaypoints();
    }

    public Driver getPartnerFromPrincipal(String name) {
        Driver currentDriver = getDriverByPrincipalName(name);
        List<Driver> partners = getCurrentOrderFromPrincipal(name).getDrivers();
        for (Driver driver : partners) {
            if (driver.equals(currentDriver)) {
                partners.remove(driver);
            }
        }
        return partners.get(0);
    }

    public List<Driver> getListOfDrivers() {
        return driverDaoImpl.findAll();
    }

    public void deleteById(Long id) {
        driverDaoImpl.delete(driverDaoImpl.findById(id));
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

}
