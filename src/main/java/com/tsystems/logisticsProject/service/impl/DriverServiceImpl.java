package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dto.*;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.mapper.*;
import com.tsystems.logisticsProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DriverServiceImpl implements DriverService {

    private ApplicationEventPublisher applicationEventPublisher;
    private DriverMapper driverMapper;
    private DriverAdminMapper driverAdminMapper;
    private DriverShortMapper driverShortMapper;
    private OrderDriverMapper orderDriverMapper;
    private WaypointMapper waypointMapper;

    private UserService userService;
    private OrderService orderService;
    private WaypointService waypointService;
    private DriverDao driverDao;

    @Autowired
    public void setDependencies(DriverDao driverDao, DriverMapper driverMapper, DriverAdminMapper driverAdminMapper,
                                UserService userService, OrderService orderService, WaypointService waypointService,
                                ApplicationEventPublisher applicationEventPublisher, DriverShortMapper driverShortMapper,
                                OrderDriverMapper orderDriverMapper, WaypointMapper waypointMapper) {
        this.driverDao = driverDao;
        this.driverMapper = driverMapper;
        this.driverAdminMapper = driverAdminMapper;
        this.driverShortMapper = driverShortMapper;
        this.orderDriverMapper = orderDriverMapper;
        this.userService = userService;
        this.orderService = orderService;
        this.waypointService = waypointService;
        this.waypointMapper = waypointMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void ScheduledTasks() {
        List<Driver> listOfAllDrivers = driverDao.findAll();
        for(Driver driver: listOfAllDrivers) {
            driver.setHoursThisMonth(0);
            driverDao.update(driver);
            applicationEventPublisher.publishEvent(new UpdateEvent());
        }
    }

    @Transactional
    public List<DriverAdminDto> getListOfDrivers() {
        List<DriverAdminDto> driversDto = new ArrayList<>();
        List<Driver> listOfDrivers = driverDao.findAll();
        for (Driver driver: listOfDrivers) {
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
    public boolean checkEditedTelephoneNumber(String telephoneNumber, Long id) {
        return driverDao.checkEditedTelephoneNumber(telephoneNumber, id);
    }

    @Transactional
    public void update(Driver driver) {
        driverDao.update(driver);
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public void add(DriverAdminDto driverAdminDto) {
        driverDao.add(driverAdminMapper.toEntity(driverAdminDto));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public boolean findDriverByTelephoneNumber(String telephoneNumber) {
        return driverDao.findByTelephoneNubmer(telephoneNumber);
    }

    @Transactional
    public void update(DriverAdminDto driverAdminDto) {
        update(driverAdminMapper.toEntity(driverAdminDto));
    }

    @Transactional
    public void update(DriverDto driverDto) {
        driverDao.update(driverMapper.toEntity(driverDto));
    }

    @Transactional
    public void editState(Long id, DriverState state) {
        DriverDto driverToUpdate = driverMapper.toDto(driverDao.findById(id));
        String lastState = driverToUpdate.getDriverState();
        if ((state == DriverState.REST) || state == DriverState.SECOND_DRIVER) {
            if (lastState.equals(DriverState.DRIVING.toString()) || lastState.equals(DriverState.LOADING_UNLOADING.toString())) {
                Date endWorkingTime = new Date();
                Date startWorkingTime = new Date(driverToUpdate.getStartWorkingTime());
                Long totalWorkingTimePerInterval = endWorkingTime.getTime() - startWorkingTime.getTime();
                int totalWorkingHoursPerInterval = (int) Math.ceil(totalWorkingTimePerInterval / 1000 / 60 / 60);
                driverToUpdate.setHoursThisMonth(driverToUpdate.getHoursThisMonth() + totalWorkingHoursPerInterval);
                driverToUpdate.setDriverState(state.toString());
                update(driverToUpdate);
            }
            driverToUpdate.setDriverState(state.toString());
            update(driverToUpdate);
        } else {
            if (lastState.equals(DriverState.REST.toString()) || lastState.equals(DriverState.SECOND_DRIVER.toString())) {
                Date startWorkingTime = new Date();
                driverToUpdate.setStartWorkingTime(startWorkingTime.getTime());
                driverToUpdate.setDriverState(state.toString());
                update(driverToUpdate);
            }
            driverToUpdate.setDriverState(state.toString());
            update(driverToUpdate);
        }
    }

    @Transactional
    public void finishOrder(OrderDriverDto orderDriverDto) {
        if (orderDriverDto != null) {
            for (WaypointDto waypointDto : orderDriverDto.getWaypoints()) {
                waypointDto.setStatus(WaypointStatus.DONE.toString());
                waypointService.update(waypointDto);
            }
            for (DriverShortDto driverDto : orderDriverDto.getDrivers()) {
                driverDto.setOrderNumber(null);
                driverDao.update(driverShortMapper.toEntity(driverDto));
                editState(driverDto.getId(), DriverState.REST);
            }
            orderDriverDto.setTruckNumber(null);
            orderDriverDto.setStatus(OrderStatus.COMPLETED.toString());
            orderDriverDto.setCompletionDate(new Date().getTime());
            orderService.update(orderDriverMapper.toEntity(orderDriverDto));
        }
    }

    @Transactional
    public List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver) {
        return driverDao.findDriversForTruck(city, maxSpentTimeForDriver);
    }

    @Transactional
    public void startOrder(Long orderId) {
        orderService.startOrder(orderId);
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
