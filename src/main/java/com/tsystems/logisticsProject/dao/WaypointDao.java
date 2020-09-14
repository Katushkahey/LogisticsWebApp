package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface WaypointDao extends GenericDao<Waypoint> {

    Waypoint findById(Long id);

    List<Waypoint> getListOfWaypointsByOrderId(Long orderId);
}
