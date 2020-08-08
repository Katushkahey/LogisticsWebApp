package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.DriverDaoImpl;
import com.tsystems.logisticsProject.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl {

    private DriverDaoImpl driverDaoImpl;
    private UserServiceImpl userService;

    @Autowired
    public void setDriverDao(DriverDaoImpl driverDao, UserServiceImpl userService) {
        this.driverDaoImpl = driverDao;
        this.userService = userService;
    }

    public Driver getDriverByPrincipalName(String name) {
        User userPrincipal = userService.findByUsername(name);
        return driverDaoImpl.findByUser(userPrincipal);
    }

    public Order getCurrentOrderFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getCurrentOrder();
    }

    public List<Waypoint> getListOfWaypointsFromPrincipal(String name) {
        return getCurrentOrderFromPrincipal(name).getWaypoints();
    }

    public String getDriverNameFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getName();
    }

    public String getDriverSurnameFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getSurname();
    }

    public Long getOrderIdFromPrincipal(String name) {
        return getCurrentOrderFromPrincipal(name).getId();
    }

    public String getTelephoneNumberFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getTelephoneNumber();
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

    public String getCurrentTruckNumberFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getCurrentOrder().getOrderTruck().getNumber();
    }
}
