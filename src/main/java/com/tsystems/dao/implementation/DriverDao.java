package com.tsystems.dao.implementation;

import com.tsystems.entity.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DriverDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional
    public Driver findById(int id) {
        return sessionFactory.openSession().get(Driver.class, id);
    }

    @Transactional
    public void save(Driver driver) {
        Session session = sessionFactory.openSession();
        session.save(driver);
    }

    @Transactional
    public void update(Driver driver) {
        Session session = sessionFactory.openSession();
        session.update(driver);
    }

    @Transactional
    public void delete(Driver driver) {
        Session session = sessionFactory.openSession();
        session.delete(driver);
    }
}
