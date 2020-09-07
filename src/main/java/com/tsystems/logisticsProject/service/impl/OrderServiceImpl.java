package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.OrderClientDto;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.mapper.OrderClientMapper;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.InfoboardService;
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

    private OrderDao orderDao;
    private OrderClientMapper orderClientMapper;
    private DriverService driverService;
    private WaypointService waypointService;
    private InfoboardService infoboardService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setDependencies(OrderDao orderDao, DriverService driverService, WaypointService waypointService,
                                ApplicationEventPublisher applicationEventPublisher, OrderClientMapper orderClientMapper,
                                InfoboardService infoboardService) {
        this.orderDao = orderDao;
        this.driverService = driverService;
        this.waypointService = waypointService;
        this.applicationEventPublisher  =applicationEventPublisher;
        this.infoboardService = infoboardService;
        this.orderClientMapper = orderClientMapper;
    }

//    @Transactional
//    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
//        return waypointService.getListOfWaypointsByOrderId(id);
//    }

//    @Transactional
//    public HashMap<Order, Double> findCompletedOrders() {
//        HashMap<Order, Double> completedOrderHashMap = new HashMap<>();
//        List<Order> completedOrders = orderDao.findCompetedOrders();
//        for (Order order : completedOrders) {
//            completedOrderHashMap.put(order, getMaxWeightForOrderById(order.getId()));
//        }
//        return completedOrderHashMap;
//    }

//    @Transactional
//    public HashMap<Order, Double> findUnassignedOrders() {
//        HashMap<Order, Double> unassignedOrderHashMap = new HashMap<>();
//        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
//        for (Order order : unassignedOrders) {
//            unassignedOrderHashMap.put(order, getMaxWeightForOrderById(order.getId()));
//        }
//        return unassignedOrderHashMap;
//    }

//    @Transactional
//    public HashMap<Order, Double> findWaitingOrders() {
//        HashMap<Order, Double> waytingOrderHashMap = new HashMap<>();
//        List<Order> waytingOrders = orderDao.findWaitingOrders();
//        for (Order order : waytingOrders) {
//            waytingOrderHashMap.put(order, getMaxWeightForOrderById(order.getId()));
//        }
//        return waytingOrderHashMap;
//
//    }

//    @Transactional
//    public HashMap<Order, Double> findOrdersInProgress() {
//        HashMap<Order, Double> ordersInProgressHashMap = new HashMap<>();
//        List<Order> ordersInProgress = orderDao.findOrdersInProgress();
//        for (Order order : ordersInProgress) {
//            ordersInProgressHashMap.put(order, getMaxWeightForOrderById(order.getId()));
//        }
//        return ordersInProgressHashMap;
//    }

//    @Transactional
//    public HashMap<Order, List<Waypoint>> findListOfWaypointsForCompletedOrders() {
//        List<Order> completedOrders = orderDao.findCompetedOrders();
//        return findListOfWaypointsForOrders(completedOrders);
//    }

//    @Transactional
//    public HashMap<Order, List<Waypoint>> findListOfWaypointsForWaytingOrders() {
//        List<Order> waitingOrders = orderDao.findWaitingOrders();
//        return findListOfWaypointsForOrders(waitingOrders);
//    }

//    @Transactional
//    public HashMap<Order, List<Waypoint>> findListOfWaypointsForOrdersInProgress() {
//        List<Order> ordersInProgress = orderDao.findOrdersInProgress();
//        return findListOfWaypointsForOrders(ordersInProgress);
//    }

//    @Transactional
//    public HashMap<Order, List<Waypoint>> findListOfWaypointsForUnassignedOrders() {
//        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
//        return findListOfWaypointsForOrders(unassignedOrders);
//    }

//    @Transactional
//    public HashMap<Order, List<Waypoint>> findListOfWaypointsForOrders(List<Order> listOfOrders) {
//        HashMap<Order, List<Waypoint>> mapOfWaypoints = new HashMap<>();
//        for (Order order : listOfOrders) {
//            mapOfWaypoints.put(order, findWaypointsForCurrentOrderById(order.getId()));
//        }
//        return mapOfWaypoints;
//    }

//    @Transactional
//    public List<Cargo> getListOfCargoesForCurrentOrderById(Long id) {
//        return findById(id).getCargoes();
//    }

//    @Transactional
//    public double getMaxWeightForOrderById(Long id) {
//        double maxWeight = 0;
//        double totalWeight = 0;
//        List<Waypoint> waypoints = findWaypointsForCurrentOrderById(id);
//        for (Waypoint waypoint : waypoints) {
//            if (waypoint.getAction().equals(Action.LOADING)) {
//                totalWeight += waypoint.getCargo().getWeight();
//                if (maxWeight < totalWeight) {
//                    maxWeight = totalWeight;
//                }
//            } else {
//                totalWeight -= waypoint.getCargo().getWeight();
//            }
//        }
//        return maxWeight;
//    }

    @Transactional
    public void deleteById(Long id) {
        orderDao.delete(orderDao.findById(id));
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent(this));
    }

//    @Transactional
//    public Map<Order, List<Driver>> getMapOfDriversForWaitingOrders() {
//        Map<Order, List<Driver>> mapOfDriversForWaitingOrders = new HashMap<>();
//        List<Order> waytingOrders = orderDao.findWaitingOrders();
//        for (Order order : waytingOrders) {
//            mapOfDriversForWaitingOrders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
//        }
//        return mapOfDriversForWaitingOrders;
//    }

//    @Transactional
//    public Map<Order, List<Driver>> getMapOfDriversForUnassignedOrders() {
//        Map<Order, List<Driver>> mapOfDriversForUnassignedOders = new HashMap<>();
//        List<Order> unassignedOrders = orderDao.findUnassignedOrders();
//        for (Order order : unassignedOrders) {
//            mapOfDriversForUnassignedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
//        }
//        return mapOfDriversForUnassignedOders;
//    }

//    @Transactional
//    public Map<Order, List<Driver>> getMapOfDriversForCompletedOrders() {
//        Map<Order, List<Driver>> mapOfDriversForCompletedOders = new HashMap<>();
//        List<Order> completedOrders = orderDao.findCompetedOrders();
//        for (Order order : completedOrders) {
//            mapOfDriversForCompletedOders.put(order, driverService.getParnersForCurrentOrder(order.getId()));
//        }
//        return mapOfDriversForCompletedOders;
//    }

//    @Transactional
//    public Map<Order, List<Driver>> getMapOfDriversForOrdersInProgress() {
//        Map<Order, List<Driver>> mapOfDriversForOrdersInProgress = new HashMap<>();
//        List<Order> ordersInProgress = orderDao.findOrdersInProgress();
//        for (Order order : ordersInProgress) {
//            mapOfDriversForOrdersInProgress.put(order, driverService.getParnersForCurrentOrder(order.getId()));
//        }
//        return mapOfDriversForOrdersInProgress;
//    }

    @Transactional
    public Order findById(Long id) {
        return orderDao.findById(id);
    }

    @Transactional
    public void update(Order order) {
        orderDao.update(order);
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent(this));
    }

    @Transactional
    public void startOrder(Long id) {
        Order order = findById(id);
        order.setStatus(OrderStatus.IN_PROGRESS);
        update(order);
    }

    @Transactional
    public void add(Order order) {
        orderDao.add(order);
        infoboardService.updateInfoboard();
        applicationEventPublisher.publishEvent(new UpdateEvent(this));
    }

    @Transactional
    public void assign(Long orderId, Truck truck, List<Driver> listOfDrivers) {
        Order orderToUpdate = findById(orderId);
        orderToUpdate.setOrderTruck(truck);
        orderToUpdate.setDrivers(listOfDrivers);
        orderToUpdate.setStatus(OrderStatus.WAITING);
        for (Driver driver : listOfDrivers) {
            driver.setCurrentOrder(orderToUpdate);
            driverService.update(driver);
        }
        update(orderToUpdate);
    }

//    @Transactional
//    public void cancelAssignment(Long orderId) {
//        Order orderToUpdate = findById(orderId);
//        orderToUpdate.setOrderTruck(null);
//        orderToUpdate.setStatus(OrderStatus.NOT_ASSIGNED);
//        orderToUpdate.setDrivers(null);
//        List<Driver> listOfDrivers = driverService.getParnersForCurrentOrder(orderId);
//        for(Driver driver: listOfDrivers) {
//            driver.setCurrentOrder(null);
//            driverService.update(driver);
//        }
//        update(orderToUpdate);
//    }

    @Transactional
    public boolean deleteWaypoint(Long orderId, Long waypointId) {
        return waypointService.deleteWaypoint(orderId, waypointId);
    }

    @Transactional
    public List<OrderClientDto> getTopOrders() {
        List<Order> listOfTopOrders = orderDao.getTopOrders(REQUIRED_NUMBER);
        List<OrderClientDto> listOfTopOrdersDto = new ArrayList<>();
        for (Order order: listOfTopOrders) {
            listOfTopOrdersDto.add(orderClientMapper.toDto(order));
        }
        return listOfTopOrdersDto;
    }

}
