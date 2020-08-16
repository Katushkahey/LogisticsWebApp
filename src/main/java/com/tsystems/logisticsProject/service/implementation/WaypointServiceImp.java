package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.entity.Waypoint;

import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaypointServiceImp implements WaypointService {

    @Autowired
    WaypointDao waypointDao;

    @Transactional
    public Waypoint findById(Long id) {
        return waypointDao.findById(id);
    }

    @Transactional
    public void update(Waypoint waypoint) {
        waypointDao.update(waypoint);
    }

    @Transactional
    public void makeCompletedById(Long id) {
        Waypoint completedWaypoint = findById(id);
        completedWaypoint.setStatus(WaypointStatus.DONE);
        update(completedWaypoint);
    }

    @Transactional
    public List<Waypoint> getListOfWaypointsByOrderId(Long orderId) {
        return waypointDao.getListOfWaypointsByOrderId(orderId);
    }
}
