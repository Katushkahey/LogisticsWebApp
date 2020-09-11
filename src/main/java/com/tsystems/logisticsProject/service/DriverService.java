package com.tsystems.logisticsProject.service;


import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;

import java.util.LinkedHashMap;
import java.util.List;

public interface DriverService {

    DriverDto getDriverByPrincipalName(String name);

    List<DriverAdminDto> getListOfDrivers();

    void deleteById(Long id);

    boolean checkEditedTelephoneNumber(String telephoneNumber, Long id);

    void add(DriverAdminDto driverAdminDto);

    boolean findDriverByTelephoneNumber(String telephineNumber);

    void update(DriverAdminDto driverAdminDto);

    void update(DriverDto driverDto);

    void editState(Long id, DriverState state);

    void finishOrder(OrderDriverDto orderDriverDto);

    List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver);

    void update(Driver driver);

    void startOrder(Long id);

    LinkedHashMap<String, Long> getDriversInfo();
}
