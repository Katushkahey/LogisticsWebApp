package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.entity.Waypoint;

import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaypointServiceImp implements WaypointService {

    @Transactional
    public void update(Waypoint waypoint) {

    }
}
