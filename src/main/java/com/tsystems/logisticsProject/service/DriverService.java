package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.*;

import java.util.LinkedHashMap;
import java.util.List;

public interface DriverService {

    DriverDto getDriverByPrincipalName(String name);

    List<DriverAdminDto> getListOfDrivers();

    void ScheduledTasks();

    void deleteById(Long id);

    boolean checkEditedTelephoneNumber(String telephoneNumber, Long id);

    void add(DriverAdminDto driverAdminDto);

    void update(DriverAdminDto driverAdminDto);

    void update(DriverDto driverDto);

    List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver);

    LinkedHashMap<String, Long> getDriversInfo();
}
