package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class DriverDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public Driver findById(int id) {
        return  sessionFactory.openSession().get(Driver.class, id);
    }

    public void save(Driver driver) {
        sessionFactory.openSession().save(driver);
    }

    public void update(Driver driver) {
        sessionFactory.openSession().update(driver);
    }

    public void delete(Driver driver) {
        sessionFactory.openSession().delete(driver);
    }

    public Driver findByUser(User user) {
        return sessionFactory.openSession().createQuery("SELECT d FROM Driver d WHERE d.user=:user", Driver.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Driver> findAll() {
        return sessionFactory.openSession().createQuery("SELECT d FROM Driver d", Driver.class)
                .getResultList();
    }

}
