package com.tsystems.logisticsProject.service;


import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.dto.DriverDtoForDriverPage;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;

import java.util.List;

public interface DriverService {

    Driver findById(Long id);

    DriverDtoForDriverPage getDriverByPrincipalName(String name);

    List<DriverDto> getListOfDrivers();

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

    void update(Driver driver);

    void startOrder(Long id);
}
