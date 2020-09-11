package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.dto.OrderAdminDto;
import com.tsystems.logisticsProject.dto.OrderClientDto;
import com.tsystems.logisticsProject.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {

    List<Waypoint> findWaypointsForCurrentOrderById(Long id);

    OrderAdminDto findById(Long id);

    OrderAdminDto findByNumber(String number);

    double getMaxWeightForOrderById(List<Waypoint> listOfWaypoint);

    void deleteById(Long id);

    List<OrderAdminDto> getListOfWaitingOrders();

    List<OrderAdminDto> getListOfUnassignedOrders();

    List<OrderAdminDto> getListOfCompletedOrders();

    List<OrderAdminDto> getListOfOrdersInProgress();

    void add(Order order);

    void update(Order order);

    void startOrder(Long id);

    void assign(OrderAdminDto orderDto, CombinationForOrderDto cf);

    void cancelAssignment(OrderAdminDto orderAdminDto);

    boolean deleteWaypoint(Long orderId, Long waypointId);

    List<OrderClientDto> getTopOrders();
}
