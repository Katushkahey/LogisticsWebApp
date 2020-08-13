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

    @Override
    public void add(Waypoint waypoint) {
        sessionFactory.getCurrentSession().save(waypoint);
    }

    @Override
    public void update(Waypoint waypoint) {
        sessionFactory.getCurrentSession().update(waypoint);
    }

    @Override
    public void delete(Waypoint waypoint) {
        sessionFactory.getCurrentSession().delete(waypoint);
    }
}
