package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DriverDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public Driver findById(int id) {
        return sessionFactory.openSession().get(Driver.class, id);
    }

    public void save(Driver driver) {
        Session session = sessionFactory.openSession();
        session.save(driver);
    }

    public void update(Driver driver) {
        Session session = sessionFactory.openSession();
        session.update(driver);
    }

    public void delete(Driver driver) {
        Session session = sessionFactory.openSession();
        session.delete(driver);
    }
}
