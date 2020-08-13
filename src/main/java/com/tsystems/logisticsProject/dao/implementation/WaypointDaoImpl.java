package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WaypointDaoImpl extends AbstractDao<Waypoint> implements WaypointDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void add(Waypoint waypoint) {
        sessionFactory.getCurrentSession().save(waypoint);
    }

    public void update(Waypoint waypoint) {
        sessionFactory.getCurrentSession().update(waypoint);
    }

    public void delete(Waypoint waypoint) {
        sessionFactory.getCurrentSession().delete(waypoint);
    }

    public Waypoint findById(Long id) {
        return sessionFactory.getCurrentSession().get(Waypoint.class, id);
    }
}

