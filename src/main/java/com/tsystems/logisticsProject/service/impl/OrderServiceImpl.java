package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.*;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.mapper.*;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final int REQUIRED_NUMBER = 10;

    private ApplicationEventPublisher applicationEventPublisher;
    private OrderClientMapper orderClientMapper;
    private OrderAdminMapper orderAdminMapper;
    private OrderDriverMapper orderDriverMapper;
    private DriverShortMapper driverShortMapper;
    private DriverService driverService;
    private WaypointService waypointService;
    private OrderDao orderDao;

    @Autowired
    public void setDependencies(OrderDao orderDao, DriverService driverService, WaypointService waypointService,
                                ApplicationEventPublisher applicationEventPublisher, OrderClientMapper orderClientMapper,
                                OrderAdminMapper orderAdminMApper, OrderDriverMapper orderDriverMapper,
                                DriverShortMapper driverShortMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderClientMapper = orderClientMapper;
        this.orderAdminMapper = orderAdminMApper;
        this.orderDriverMapper = orderDriverMapper;
        this.driverShortMapper = driverShortMapper;
        this.driverService = driverService;
        this.waypointService = waypointService;
        this.orderDao = orderDao;
    }

    @Transactional
    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return waypointService.getListOfWaypointsByOrderId(id);
    }

    @Transactional
    public double getMaxWeightForOrderById(List<Waypoint> listOfWaypoint) {
        double maxWeight = 0;
        double totalWeight = 0;
        for (Waypoint waypoint : listOfWaypoint) {
            if (waypoint.getAction().equals(Action.LOADING)) {
                totalWeight += waypoint.getCargo().getWeight();
                if (maxWeight < totalWeight) {
                    maxWeight = totalWeight;
                }
            } else {
                totalWeight -= waypoint.getCargo().getWeight();
            }
        }
        return maxWeight;
    }

    @Transactional
    public void deleteById(Long id) {
        orderDao.delete(orderDao.findById(id));
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public OrderAdminDto findById(Long id) {
        return orderAdminMapper.toDto(orderDao.findById(id));
    }

    @Transactional
    public void update(Order order) {
        orderDao.update(order);
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public void startOrder(Long id) {
        Order order = orderDao.findById(id);
        OrderDriverDto orderDto = orderDriverMapper.toDto(order);
        orderDto.setStatus(OrderStatus.IN_PROGRESS.toString());
        update(orderDriverMapper.toEntity(orderDto));
    }

    @Transactional
    public void add(Order order) {
        orderDao.add(order);
        applicationEventPublisher.publishEvent(new UpdateEvent());
    }

    @Transactional
    public void assign(OrderAdminDto orderDto, CombinationForOrderDto cf) {
        orderDto.setTruckNumber(cf.getTruckNumber());
        orderDto.setDrivers(cf.getDrivers());
        orderDto.setStatus(OrderStatus.WAITING.toString());
        for (DriverShortDto driver : cf.getDrivers()) {
            driver.setOrderNumber(orderDto.getNumber());
            driverService.update(driverShortMapper.toEntity(driver));
        }
        update(orderAdminMapper.toEntity(orderDto));
    }

    @Transactional
    public void cancelAssignment(OrderAdminDto orderAdminDto) {
        orderAdminDto.setTruckNumber(null);
        orderAdminDto.setStatus(OrderStatus.NOT_ASSIGNED.toString());
        orderAdminDto.setDrivers(null);
        for (DriverShortDto driverDto : orderAdminDto.getDrivers()) {
            driverDto.setOrderNumber(null);
            driverService.update(driverShortMapper.toEntity(driverDto));
        }
        update(orderAdminMapper.toEntity(orderAdminDto));
    }

    @Transactional
    public boolean deleteWaypoint(Long orderId, Long waypointId) {
        return waypointService.deleteWaypoint(orderId, waypointId);
    }

    @Transactional
    public List<OrderClientDto> getTopOrders() {
        List<Order> listOfTopOrders = orderDao.getTopOrders(REQUIRED_NUMBER);
        List<OrderClientDto> listOfTopOrdersDto = new ArrayList<>();
        for (Order order : listOfTopOrders) {
            listOfTopOrdersDto.add(orderClientMapper.toDto(order));
        }
        return listOfTopOrdersDto;
    }

    @Transactional
    public List<OrderAdminDto> getListOfWaitingOrders() {
        List<Order> listOfOrders = orderDao.findWaitingOrders();
        return convertListOfOrdersToListOfOrderAdminDto(listOfOrders);
    }

    @Transactional
    public List<OrderAdminDto> getListOfUnassignedOrders() {
        List<Order> listOfOrders = orderDao.findUnassignedOrders();
        return convertListOfOrdersToListOfOrderAdminDto(listOfOrders);
    }

    @Transactional
    public List<OrderAdminDto> getListOfCompletedOrders() {
        List<Order> listOfOrders = orderDao.findCompetedOrders();
        return convertListOfOrdersToListOfOrderAdminDto(listOfOrders);
    }

    @Transactional
    public List<OrderAdminDto> getListOfOrdersInProgress() {
        List<Order> listOfOrders = orderDao.findOrdersInProgress();
        return convertListOfOrdersToListOfOrderAdminDto(listOfOrders);
    }

    private List<OrderAdminDto> convertListOfOrdersToListOfOrderAdminDto(List<Order> listOfOrders) {
        List<OrderAdminDto> listOfOrderAdminDto = new ArrayList<>();
        for (Order order : listOfOrders) {
            listOfOrderAdminDto.add(orderAdminMapper.toDto(order));
        }
        return listOfOrderAdminDto;
    }

}
