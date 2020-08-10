package com.tsystems.logisticsProject.service.abstraction;


import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface DriverService {

    public Driver getDriverByPrincipalName(String name);

    public List<Waypoint> getListOfWaypointsFromPrincipal(String name);

    public Driver getPartnerFromPrincipal(String name);

    public List<Driver> getListOfDrivers();

    public void deleteById(Long id);
}
