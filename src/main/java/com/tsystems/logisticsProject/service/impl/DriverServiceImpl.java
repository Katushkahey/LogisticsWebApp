package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dto.*;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.exception.checked.NotUniqueDriverTelephoneNumberException;
import com.tsystems.logisticsProject.exception.checked.NotUniqueUserNameException;
import com.tsystems.logisticsProject.mapper.*;
import com.tsystems.logisticsProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.logging.Logger;

@Service
public class DriverServiceImpl implements DriverService {

    private static final Logger LOG = Logger.getLogger(DriverServiceImpl.class.getName());

    private ApplicationEventPublisher applicationEventPublisher;
    private DriverMapper driverMapper;
    private DriverAdminMapper driverAdminMapper;

    private UserService userService;
    private DriverDao driverDao;

    @Autowired
    public void setDependencies(DriverDao driverDao, DriverMapper driverMapper, DriverAdminMapper driverAdminMapper,
                                UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.driverDao = driverDao;
        this.driverMapper = driverMapper;
        this.driverAdminMapper = driverAdminMapper;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * reset number of working hours for all drivers on first day of every month
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void ScheduledTasks() {
        List<Driver> listOfAllDrivers = driverDao.findAll();
        for (Driver driver : listOfAllDrivers) {
            driver.setHoursThisMonth(0);
            LOG.info("All driver`s working hours have been reset");
            driverDao.update(driver);
            applicationEventPublisher.publishEvent(new UpdateEvent());
        }
    }

    @Transactional
    public List<DriverAdminDto> getListOfDrivers() {
        List<DriverAdminDto> driversDto = new ArrayList<>();
        List<Driver> listOfDrivers = driverDao.findAll();
        for (Driver driver : listOfDrivers) {
            driversDto.add(driverAdminMapper.toDto(driver));
        }
        return driversDto;
    }

    @Transactional
    public DriverDto getDriverByPrincipalName(String name) {
        User user = userService.findByUsername(name);
        return driverMapper.toDto(driverDao.findByUser(user));
    }

    @Transactional
    public void deleteById(Long id) {
        driverDao.delete(driverDao.findById(id));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public void update(Driver driver) {
        driverDao.update(driver);
    }

    @Transactional
    public void add(DriverAdminDto driverAdminDto) {
        driverDao.add(driverAdminMapper.toEntity(driverAdminDto));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    /**
     *  tries to get driver with current telephoneNumber
     * @throws NotUniqueDriverTelephoneNumberException if driver with current telephoneNumber was found
     */
    @Transactional
    public void checkTelephoneNumberToCreateDriver(String telephoneNumber) throws NotUniqueDriverTelephoneNumberException {
        try {
            driverDao.findByTelephoneNubmer(telephoneNumber);
            throw new NotUniqueDriverTelephoneNumberException(telephoneNumber);
        } catch (NoResultException e) {
            LOG.info("telephoneNumber is unique :" + telephoneNumber);
        }
    }

    /**
     *  tries to get user with current username
     * @throws NotUniqueUserNameException if user with current username was found
     */
    public void checkUserNameToCreateDriver(String userName) throws NotUniqueUserNameException {
        try {
            userService.findByUsername(userName);
            throw new NotUniqueUserNameException(userName);
        } catch (NoResultException exception) {
            LOG.info("user with this userName wasn`t found");
        }
    }

    @Transactional
    public void createNewUser(String username) {
        userService.add(username, "ROLE_DRIVER");
    }

    /**
     * tries to find another driver with the same telephone number
     * before update driver
     * @throws NotUniqueDriverTelephoneNumberException if another driver with the telephone same number was found
     */
    @Transactional
    public void update(DriverAdminDto driverAdminDto) throws NotUniqueDriverTelephoneNumberException {
        try {
            Driver driver = driverDao.findByTelephoneNubmer(driverAdminDto.getTelephoneNumber());
            if (driver.getId() != driverAdminDto.getId()) {
                throw new NotUniqueDriverTelephoneNumberException(driverAdminDto.getTelephoneNumber());
            } else {
                update(driverAdminMapper.toEntity(driverAdminDto));
            }
        } catch (NoResultException e) {
            update(driverAdminMapper.toEntity(driverAdminDto));
        }
    }

    @Transactional
    public void update(DriverDto driverDto) {
        driverDao.update(driverMapper.toEntity(driverDto));
    }

    @Transactional
    public List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver) {
        return driverDao.findDriversForTruck(city, maxSpentTimeForDriver);
    }

    @Transactional
    public LinkedHashMap<String, Long> getDriversInfo() {
        LinkedHashMap<String, Long> mapOfDrivers = new LinkedHashMap<>();
        mapOfDrivers.put("Available", driverDao.getAvailableDrivers(Driver.MAX_HOURS_IN_MONTH));
        mapOfDrivers.put("Employed", driverDao.getEmployedDrivers());
        mapOfDrivers.put("WorkedEnough", driverDao.getDriversWorkedEnough(Driver.MAX_HOURS_IN_MONTH));
        return mapOfDrivers;
    }
}
