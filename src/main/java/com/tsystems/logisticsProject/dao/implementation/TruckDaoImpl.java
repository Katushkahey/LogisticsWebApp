package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TruckDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    OrderDaoImpl orderDaoImpl;

    public Truck findById(Long id) {
        return sessionFactory.getCurrentSession().get(Truck.class, id);
    }

    public void save(Truck truck) {
        sessionFactory.getCurrentSession().save(truck);
    }

    public void update(Truck truck) {
        sessionFactory.getCurrentSession().update(truck);
    }

    public void delete(Truck truck) {
        sessionFactory.getCurrentSession().delete(truck);
    }

    public List<Truck> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t", Truck.class)
                .getResultList();
    }
}
