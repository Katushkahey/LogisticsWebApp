package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;

public interface CargoService {

    void addWaypoint(Cargo cargo, Waypoint waypoint);
}
