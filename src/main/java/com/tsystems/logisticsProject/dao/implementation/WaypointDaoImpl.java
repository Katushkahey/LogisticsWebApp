package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

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

    // select*from waypoints where cargo_id in (select * from cargoes where order_id = orderId)
    public List<Waypoint> getListOfWaypointsByOrderId(Long orderId) {
        try {
            Query query1 = sessionFactory.getCurrentSession().createQuery("SELECT c FROM Cargo c WHERE c.order.id=:id", Cargo.class)
                    .setParameter("id", orderId);
            return sessionFactory.getCurrentSession().createQuery("SELECT w FROM Waypoint w WHERE w.cargo in (:ids) order by w.sequence", Waypoint.class)
                    .setParameterList("ids", ((org.hibernate.query.Query) query1).list())
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}

