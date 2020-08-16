package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Waypoint;

import java.util.List;

public interface WaypointDao extends GenericDao<Waypoint> {

    void update(Waypoint waypoint);

    Waypoint findById(Long id);

    List<Waypoint> getListOfWaypointsByOrderId(Long orderId);
}
