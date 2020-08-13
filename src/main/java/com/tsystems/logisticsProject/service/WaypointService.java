package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Waypoint;

public interface WaypointService {

    void update(Waypoint waypoint);

    void makeCompletedById(Long id);

    Waypoint findById(Long id);

}
