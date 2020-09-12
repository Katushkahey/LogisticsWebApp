package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;

import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.mapper.WaypointMapper;
import com.tsystems.logisticsProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaypointServiceImpl implements WaypointService {

    private WaypointDao waypointDao;
    private WaypointMapper waypointMapper;
    private OrderDao orderDao;
    private OrderService orderService;
    private CargoService cargoService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setDependencies(WaypointDao waypointDao, OrderService orderService, CargoService cargoService,
                                ApplicationEventPublisher applicationEventPublisher, OrderDao orderDao,
                                WaypointMapper waypointMapper) {
        this.waypointDao = waypointDao;
        this.cargoService = cargoService;
        this.orderService = orderService;
        this.orderDao = orderDao;
        this.waypointMapper = waypointMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void update(WaypointDto waypointDto) {
        waypointDao.update(waypointMapper.toEntity(waypointDto));
    }

    @Transactional
    public List<Waypoint> getListOfWaypointsByOrderId(Long orderId) {
        return waypointDao.getListOfWaypointsByOrderId(orderId);
    }

    @Transactional
    public boolean deleteWaypoint(Long orderId, Long waypointId) {
        Order orderToUpdate = orderDao.findById(orderId);
        List<Waypoint> listOfWaypoint = waypointDao.getListOfWaypointsByOrderId(orderId);
        if (listOfWaypoint.size() == 2) {
            orderService.deleteById(orderId);
            applicationEventPublisher.publishEvent(new UpdateEvent());
            return true;
        }
        Waypoint waypointToDelete = waypointDao.findById(waypointId);
        Cargo cargo = waypointToDelete.getCargo();
        orderToUpdate.getCargoes().remove(cargo);
        orderService.update(orderToUpdate);
        cargoService.delete(cargo);
        applicationEventPublisher.publishEvent(new UpdateEvent());
        return false;
    }
}
