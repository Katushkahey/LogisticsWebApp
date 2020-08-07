package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class TruckDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public Truck findById(Long id) {
        return sessionFactory.openSession().get(Truck.class, id);
    }

    public void save(Truck truck) {
        sessionFactory.openSession().save(truck);
    }

    public void update(Truck truck) {
        sessionFactory.openSession().update(truck);
    }

    public void delete(Truck truck) {
        sessionFactory.openSession().delete(truck);
    }

    public List<Truck> findAll() {
        return sessionFactory.openSession().createQuery("SELECT t FROM Truck t", Truck.class)
                .getResultList();
    }
}
