package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class TruckDaoImpl extends AbstractDao<Truck> implements TruckDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Truck findById(Long id) {
        return sessionFactory.getCurrentSession().get(Truck.class, id);
    }

    public List<Truck> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t order by t.id", Truck.class)
                .getResultList();
    }

    public Truck findByNumber(String number) {
        return sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t WHERE t.number=:number",
                Truck.class).setParameter("number", number).getSingleResult();
    }

    public List<Truck> findTrucksForOrder(double maxOneTimeWeight) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM Truck t WHERE t.capacity>=:weight AND t.truckState=:state " +
                                "AND t.id NOT IN (SELECT o.orderTruck FROM Order o where o.orderTruck is not null)",
                        Truck.class).setParameter("weight", maxOneTimeWeight)
                .setParameter("state", TruckState.OK).getResultList();
    }

    public Long getBrokenTrucks() {
        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(t) FROM Truck t WHERE t.truckState=:state",
                Long.class)
                .setParameter("state", TruckState.BROKEN)
                .getSingleResult();
    }

    public Long getAvailableTrucks() {

        Query query1 = sessionFactory.getCurrentSession().createQuery("SELECT o.orderTruck.id FROM Order o WHERE " +
                "o.orderTruck is not NULL", Long.class);

        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(t) FROM Truck t WHERE t.truckState=:state" +
                " AND t.id not in (:trucks)", Long.class)
                .setParameter("state", TruckState.OK)
                .setParameter("trucks", ((org.hibernate.query.Query) query1).list())
                .getSingleResult();
    }

    public Long getEmployedTrucks() {
        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(distinct o.orderTruck) FROM Order o ",
                Long.class).getSingleResult();
    }
}