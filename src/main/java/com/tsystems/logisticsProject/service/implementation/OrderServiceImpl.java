package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.OrderDaoImpl;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl {

    @Autowired
    OrderDaoImpl orderDaoImpl;

    public List<Waypoint> findWaypointsForCurrentOrderById(Long id) {
        return orderDaoImpl.findById(id).getWaypoints();
    }
}
