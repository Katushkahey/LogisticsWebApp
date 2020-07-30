package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TruckDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Truck findById(int id) {
        return sessionFactory.openSession().get(Truck.class, id);
    }

    @Transactional
    public void save(Truck truck) {
        Session session = sessionFactory.openSession();
        session.save(truck);
    }

    @Transactional
    public void update(Truck truck) {
        Session session = sessionFactory.openSession();
        session.update(truck);
    }

    @Transactional
    public void delete(Truck truck) {
        Session session = sessionFactory.openSession();
        session.delete(truck);
    }
}
