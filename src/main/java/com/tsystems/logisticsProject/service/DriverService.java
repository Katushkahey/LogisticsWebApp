package com.tsystems.logisticsProject.service;


import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface DriverService {

    Driver getDriverByPrincipalName(String name);

    List<Waypoint> getListOfWaypointsFromPrincipal(String name);

    Driver getPartnerFromPrincipal(String name);

    List<Driver> getListOfDrivers();

    List<Driver> getParnersForCurrentOrder(Long orderId);

    void deleteById(Long id);
}
