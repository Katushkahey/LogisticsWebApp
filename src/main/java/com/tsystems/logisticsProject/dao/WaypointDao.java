package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Waypoint;

public interface WaypointDao extends GenericDao<Waypoint> {

    void update(Waypoint waypoint);

    Waypoint findById(Long id);
}
