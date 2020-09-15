package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.exception.checked.NotUniqueDriverTelephoneNumberException;
import com.tsystems.logisticsProject.exception.checked.NotUniqueUserNameException;

import java.util.LinkedHashMap;
import java.util.List;

public interface DriverService {

    DriverDto getDriverByPrincipalName(String name);

    List<DriverAdminDto> getListOfDrivers();

    void checkUserNameToCreateDriver(String userName) throws NotUniqueUserNameException;

    void checkTelephoneNumberToCreateDriver(String telephoneNumber) throws NotUniqueDriverTelephoneNumberException;

    void createNewUser(String username);

    void ScheduledTasks();

    void deleteById(Long id);

    void add(DriverAdminDto driverAdminDto);

    void update(DriverAdminDto driverAdminDto) throws NotUniqueDriverTelephoneNumberException;

    void update(DriverDto driverDto);

    List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver);

    LinkedHashMap<String, Long> getDriversInfo();
}
