package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
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

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public DriverServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Driver getDriverByPrincipalName(String name) {
        User userPrincipal = userService.findByUsername(name);
        return driverDao.findByUser(userPrincipal);
    }

    @Transactional
    public Order getCurrentOrderFromPrincipal(String name) {
        return getDriverByPrincipalName(name).getCurrentOrder();
    }

    @Transactional
    public List<Waypoint> getListOfWaypointsFromPrincipal(String name) {
        return getCurrentOrderFromPrincipal(name).getWaypoints();
    }

    @Transactional
    public Driver getPartnerFromPrincipal(String name) {
        Driver currentDriver = getDriverByPrincipalName(name);
        Order currentOrder = getCurrentOrderFromPrincipal(name);
        Collection<Driver> partners = Collections.synchronizedCollection(getParnersForCurrentOrder(currentOrder.getId()));
        for (Driver driver : partners) {
            if (driver.equals(currentDriver)) {
                partners.remove(driver);
            }
        }
        return (Driver)partners.toArray()[0];
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
            if (driverDao.findByUser(userToReturn) == null) {
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
        driver.setHoursThisMonth(0);
        driver.setDriverState(DriverState.REST);
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
        Driver driverToUpdate = driverDao.findById(id);
        driverToUpdate.setName(name);
        driverToUpdate.setSurname(surname);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverToUpdate.setCurrentCity(cityService.findByCityName(cityName));
        driverDao.update(driverToUpdate);
    }

    @Transactional
    public void update(Long id, String telephoneNumber) {
        Driver driverToUpdate = driverDao.findById(id);
        driverToUpdate.setTelephoneNumber(telephoneNumber);
        driverDao.update(driverToUpdate);
    }

}
