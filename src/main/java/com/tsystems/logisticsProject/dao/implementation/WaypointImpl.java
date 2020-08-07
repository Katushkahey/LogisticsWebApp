package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.WaypointDao;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class WaypointImpl implements WaypointDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void add(Waypoint waypoint) {
        sessionFactory.openSession().save(waypoint);
    }

    @Override
    public void update(Waypoint waypoint) {
        sessionFactory.openSession().update(waypoint);
    }

    @Override
    public void delete(Waypoint waypoint) {
        sessionFactory.openSession().delete(waypoint);
    }
}
