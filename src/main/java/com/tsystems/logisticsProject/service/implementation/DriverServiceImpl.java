package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WaypointService waypointService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public DriverServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
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
    public Driver getDriverByPrincipalName(String name) {
        User userPrincipal = userService.findByUsername(name);
        return findByUser(userPrincipal);
    }

    @Transactional
    public Order getCurrentOrderFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getCurrentOrder();
    }

    @Transactional
    public List<Waypoint> getListOfWaypointsFromPrincipal(String name) {
        Order order = getCurrentOrderFromPrincipal(name);
        if (order == null) {
            return null;
        }
        return order.getWaypoints();
    }

    @Transactional
    public Driver getPartnerFromPrincipal(String name) {
        Driver currentDriver = getDriverByPrincipalName(name);
        Order currentOrder = getCurrentOrderFromPrincipal(name);
        if (currentOrder == null) {
            return null;
        }
        List<Driver> partners = getParnersForCurrentOrder(currentOrder.getId());
        for (Driver driver : partners) {
            if (driver.equals(currentDriver)) {
                partners.remove(driver);
                break;
            }
        }
        if (partners.size() == 0) {
            return null;
        }
        return partners.get(0);
    }

    @Transactional
    public List<Driver> getParnersForCurrentOrder(Long orderId) {
        Order order = orderService.findById(orderId);
        return driverDao.findAllDriversForCurrentOrder(order);
    }

    @Transactional
    public List<Driver> getListOfDrivers() {
        return driverDao.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        driverDao.delete(driverDao.findById(id));
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    @Transactional
    public boolean checkEditedTelephoneNumber(String telephoneNumber, Long id) {
        return driverDao.checkEditedTelephoneNumber(telephoneNumber, id);
    }

    /**
     * данный метод получает userName из формы создания Driver, проверяет есть ли в таблице Uder строка с таким userName
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
        driver.setUser(user);
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
        driverDao.update(driverToUpdate);
    }

    @Transactional
    public void update(Long id, String telephoneNumber) {
        Driver driverToUpdate = findById(id);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverDao.update(driverToUpdate);
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
                driverToUpdate.setHoursThisMonth(driverToUpdate.getHoursThisMonth() + totalWorkingTimePerInterval);
                driverToUpdate.setDriverState(state);
                driverDao.update(driverToUpdate);
            }
        } else {
            if (lastState == DriverState.REST || lastState == DriverState.SECOND_DRIVER) {
                Date startWorkingTime = new Date();
                driverToUpdate.setStartWorkingTime(startWorkingTime.getTime());
                driverToUpdate.setDriverState(state);
                driverDao.update(driverToUpdate);
            }
        }

    }

    @Transactional
    public void finishOrder(Long id) {
        Order completedOrder = orderService.findById(id);
        if (completedOrder == null) {
            return;
        }
        List<Waypoint> listOfWaypointsForCompletedOrder = completedOrder.getWaypoints();
        if (listOfWaypointsForCompletedOrder == null) {
            return;
        }
        for (Waypoint waypoint: listOfWaypointsForCompletedOrder) {
            waypoint.setStatus(WaypointStatus.DONE);
            waypointService.update(waypoint);
        }
        List<Driver> listOfDriversForCompletedOrder = getParnersForCurrentOrder(completedOrder.getId());
        if (listOfDriversForCompletedOrder == null) {
            return;
        }
        for (Driver driver: listOfDriversForCompletedOrder) {
            driver.setCurrentOrder(null);
            driverDao.update(driver);
            editState(driver.getId(), DriverState.REST);
        }
        completedOrder.setStatus(OrderStatus.COMPLETED);
        completedOrder.setCompletionDate(new Date().getTime());
        orderService.update(completedOrder);
    }

}
