package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.event.EntityUpdateEvent;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.UserService;
import org.hibernate.collection.internal.PersistentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

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
        List<Driver> partners = getParnersForCurrentOrder(currentOrder.getId());
        for (Driver driver : partners) {
            if (driver.equals(currentDriver)) {
                partners.remove(driver);
            }
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

}
