package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface WaypointService {

    void update(WaypointDto waypointDto);

//    void makeCompletedById(Long id);

    List<Waypoint> getListOfWaypointsByOrderId(Long orderId);

//    void editWaypoint(WaypointDto waypointDto);

    boolean deleteWaypoint(Long orderId, Long waypointId);
}
