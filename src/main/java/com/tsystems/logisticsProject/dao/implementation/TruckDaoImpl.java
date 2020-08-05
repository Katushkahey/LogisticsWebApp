package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class TruckDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public Truck findById(int id) {
        return sessionFactory.openSession().get(Truck.class, id);
    }

    public void save(Truck truck) {
        Session session = sessionFactory.openSession();
        session.save(truck);
    }

    public void update(Truck truck) {
        Session session = sessionFactory.openSession();
        session.update(truck);
    }

    public void delete(Truck truck) {
        Session session = sessionFactory.openSession();
        session.delete(truck);
    }
}
