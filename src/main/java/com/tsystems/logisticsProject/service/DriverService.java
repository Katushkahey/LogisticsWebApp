package com.tsystems.logisticsProject.service;


import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.User;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface DriverService {

    Driver getDriverByPrincipalName(String name);

    List<Waypoint> getListOfWaypointsFromPrincipal(String name);

    Driver getPartnerFromPrincipal(String name);

    List<Driver> getListOfDrivers();

    List<Driver> getParnersForCurrentOrder(Long orderId);

    void deleteById(Long id);

    boolean checkEditedTelephoneNumber(String telephoneNumber, Long id);

    boolean checkUserNameToCreateDriver(String userName);

    User returnUserToCreateDriver(String userName);

    void add(String name, String surname, String telephoneNumber, String cityName, User user);

    boolean findDriverByTelephoneNumber(String telephineNumber);

    void update(Long id, String name, String surname, String telephoneNumber, String cityName);
}
