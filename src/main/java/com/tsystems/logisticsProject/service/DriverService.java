package com.tsystems.logisticsProject.service;


import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;

import java.util.List;

public interface DriverService {

    Driver findById(Long id);

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

    void update(Long id, String telephoneNumber);

    void editState(Long id, DriverState state);

    void finishOrder(Long id);

    List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver);
}
