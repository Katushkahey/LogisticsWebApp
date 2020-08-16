package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface WaypointService {

    void update(Waypoint waypoint);

    void makeCompletedById(Long id);

    Waypoint findById(Long id);

    List<Waypoint> getListOfWaypointsByOrderId(Long orderId);

}
