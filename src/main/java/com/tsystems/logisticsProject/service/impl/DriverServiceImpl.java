package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.dto.DriverDtoForDriverPage;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.mapper.DriverForDriverPageMapper;
import com.tsystems.logisticsProject.mapper.DriverMapper;
import com.tsystems.logisticsProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private DriverDao driverDao;
    private DriverForDriverPageMapper mapper;
    private DriverMapper driverMapper;
    private UserService userService;
    private OrderService orderService;
    private CityService cityService;
    private WaypointService waypointService;

    @Autowired
    public void setDependencies(DriverDao driverDao, DriverForDriverPageMapper driverForDriverPageMapper, DriverMapper driverMapper,
                                UserService userService,OrderService orderService, CityService cityService, WaypointService waypointService) {
        this.driverDao = driverDao;
        this.mapper = driverForDriverPageMapper;
        this.driverMapper = driverMapper;
        this.userService = userService;
        this.orderService = orderService;
        this.cityService = cityService;
        this.waypointService = waypointService;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void ScheduledTasks() {
        List<Driver> listOfAllDrivers = driverDao.findAll();
        for(Driver driver: listOfAllDrivers) {
            driver.setHoursThisMonth(0);
            driverDao.update(driver);
        }
    }

    @Transactional
    public Driver findById(Long id) {
        return driverDao.findById(id);
    }

    @Transactional
    public Driver findByUser(User user) {
        return driverDao.findByUser(user);
    }

    @Transactional
    public List<DriverDto> getListOfDrivers() {
        List<DriverDto> driversDto = new ArrayList<>();
        List<Driver> listOfDrivers = driverDao.findAll();
        for (Driver driver: listOfDrivers) {
            driversDto.add(driverMapper.toDto(driver));
        }
        return driversDto;
    }

    @Transactional
    public DriverDtoForDriverPage getDriverByPrincipalName(String name) {
        User userPrincipal = userService.findByUsername(name);
        return mapper.toDto(findByUser(userPrincipal));
    }

    @Transactional
    public List<Driver> getParnersForCurrentOrder(Long orderId) {
        Order order = orderService.findById(orderId);
        return driverDao.findAllDriversForCurrentOrder(order);
    }

    @Transactional
    public void deleteById(Long id) {
        driverDao.delete(driverDao.findById(id));
    }

    @Transactional
    public boolean checkEditedTelephoneNumber(String telephoneNumber, Long id) {
        return driverDao.checkEditedTelephoneNumber(telephoneNumber, id);
    }

    /**
     * данный метод получает username из формы создания Driver, проверяет есть ли в таблице Uder строка с таким username
     * если да, то возвращает этого User-а, далее будет осуществлена проверка, имеется ли водитель, привязанный к этому User-у,
     * если да - вернется null, который будет расценен как ошибка в контроллере,
     * если нет - вернется этот User для дальнейшей привязки к новому водителю.
     * есди нет, то будет создан новый User с таким UserName, с дефолтным паролем "driver"(Driver сможет его поменять
     * у себя на странице) и ролью водителя.
     */

    @Transactional
    public User returnUserToCreateDriver(String userName) {
        User userToReturn = userService.returnUserToCreateDriver(userName);
        if (checkUserNameToCreateDriver(userName)) {
            if (findByUser(userToReturn) == null) {
                return userToReturn;
            }
            return null;
        }
        return userToReturn;
    }

    @Transactional
    public boolean checkUserNameToCreateDriver(String userName) {
        return userService.checkUserNameToCreateDriver(userName);
    }

    @Transactional
    public void add(String name, String surname, String telephoneNumber, String cityName, User user) {
        Driver driver = new Driver();
        driver.setName(name);
        driver.setSurname(surname);
        driver.setTelephoneNumber(telephoneNumber);
        driver.setCurrentCity(cityService.findByCityName(cityName));
        driver.setDriverState(DriverState.REST);
        driver.setUser(user);
        add(driver);
    }

    @Transactional
    public void add(Driver driver) {
        driverDao.add(driver);
    }

    @Transactional
    public boolean findDriverByTelephoneNumber(String telephoneNumber) {
        return driverDao.findByTelephoneNubmer(telephoneNumber);
    }

    @Transactional
    public void update(Long id, String name, String surname, String telephoneNumber, String cityName) {
        Driver driverToUpdate = findById(id);
        driverToUpdate.setName(name);
        driverToUpdate.setSurname(surname);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverToUpdate.setCurrentCity(cityService.findByCityName(cityName));
        update(driverToUpdate);
    }

    @Transactional
    public void update(Long id, String telephoneNumber) {
        Driver driverToUpdate = findById(id);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        update(driverToUpdate);
    }

    @Transactional
    public void editState(Long id, DriverState state) {
        Driver driverToUpdate = findById(id);
        DriverState lastState = driverToUpdate.getDriverState();
        if (state == DriverState.REST || state == DriverState.SECOND_DRIVER) {
            if (lastState == DriverState.DRIVING || lastState == DriverState.LOADING_UNLOADING) {
                Date endWorkingTime = new Date();
                Date startWorkingTime = new Date(driverToUpdate.getStartWorkingTime());
                Long totalWorkingTimePerInterval = endWorkingTime.getTime() - startWorkingTime.getTime();
                int totalWorkingHoursPerInterval = (int) Math.ceil(totalWorkingTimePerInterval / 1000 / 60 / 60);
                driverToUpdate.setHoursThisMonth(driverToUpdate.getHoursThisMonth() + totalWorkingHoursPerInterval);
                driverToUpdate.setDriverState(state);
                update(driverToUpdate);
            }
            driverToUpdate.setDriverState(state);
            update(driverToUpdate);
        } else {
            if (lastState == DriverState.REST || lastState == DriverState.SECOND_DRIVER) {
                Date startWorkingTime = new Date();
                driverToUpdate.setStartWorkingTime(startWorkingTime.getTime());
                driverToUpdate.setDriverState(state);
                update(driverToUpdate);
            }
            driverToUpdate.setDriverState(state);
            update(driverToUpdate);
        }
    }

    @Transactional
    public void finishOrder(Long id) {
        List<Waypoint> listOfWaypointsForCompletedOrder = new ArrayList<>();
        Order completedOrder = orderService.findById(id);
        if (completedOrder == null) {
            return;
        }
        List<Cargo> listOfCargoesForCompletedOrder = completedOrder.getCargoes();
        List<Driver> listOfDriversForCompletedOrder = getParnersForCurrentOrder(completedOrder.getId());
        if (listOfCargoesForCompletedOrder == null && listOfCargoesForCompletedOrder == null) {
            return;
        }
        for (Cargo cargo : listOfCargoesForCompletedOrder) {
            listOfWaypointsForCompletedOrder.add(cargo.getWaypoints().get(0));
            listOfWaypointsForCompletedOrder.add(cargo.getWaypoints().get(1));
        }
        for (Waypoint waypoint : listOfWaypointsForCompletedOrder) {
            waypoint.setStatus(WaypointStatus.DONE);
            waypointService.update(waypoint);
        }
        for (Driver driver : listOfDriversForCompletedOrder) {
            driver.setCurrentOrder(null);
            update(driver);
            editState(driver.getId(), DriverState.REST);
        }
        completedOrder.setOrderTruck(null);
        completedOrder.setStatus(OrderStatus.COMPLETED);
        completedOrder.setCompletionDate(new Date().getTime());
        orderService.update(completedOrder);
    }

    @Transactional
    public List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver) {
        return driverDao.findDriversForTruck(city, maxSpentTimeForDriver);
    }

    @Transactional
    public void update(Driver driver) {
        driverDao.update(driver);
    }

    @Transactional
    public void startOrder(Long orderId) {
        orderService.startOrder(orderId);
    }

}
