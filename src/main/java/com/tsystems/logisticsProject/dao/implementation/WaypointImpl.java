package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WaypointImpl implements WaypointDao {

    @Autowired
    Session session;

    @Override
    public void add(Waypoint waypoint) {
        session.save(waypoint);
    }

    @Override
    public void update(Waypoint waypoint) {
        session.update(waypoint);
    }

    @Override
    public void delete(Waypoint waypoint) {
        session.delete(waypoint);
    }
}
