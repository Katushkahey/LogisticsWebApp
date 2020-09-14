package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface WaypointService {

    void update(WaypointDto waypointDto);

    List<Waypoint> getListOfWaypointsByOrderId(Long orderId);

    boolean deleteWaypoint(Long orderId, Long waypointId);
}
