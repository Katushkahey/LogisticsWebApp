package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DriverDaoImpl extends AbstractDao<Driver> implements DriverDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Driver findById(Long id) {
        return sessionFactory.getCurrentSession().get(Driver.class, id);
    }

    public Driver findByUser(User user) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.user=:user", Driver.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Driver> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d order by d.id", Driver.class)
                .getResultList();
    }

    public List<Driver> findAllDriversForCurrentOrder(Order order) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.currentOrder=:order",
                Driver.class).setParameter("order", order)
                .getResultList();
    }

    public Driver findByTelephoneNubmer(String telephoneNumber) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE " +
                "d.telephoneNumber=:telephoneNumber", Driver.class).
                setParameter("telephoneNumber", telephoneNumber)
                .getSingleResult();
    }

    public List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.currentCity=:city" +
                " AND d.hoursThisMonth<=:hours and d.currentOrder is null", Driver.class)
                .setParameter("city", city)
                .setParameter("hours", maxSpentTimeForDriver)
                .getResultList();
    }

    public Long getAvailableDrivers(int hours) {
        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(d) FROM Driver d WHERE d.currentOrder is NULL " +
                "AND d.hoursThisMonth<:hours", Long.class)
                .setParameter("hours", hours)
                .getSingleResult();
    }

    public Long getEmployedDrivers() {
        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(d) FROM Driver d WHERE d.currentOrder is not NULL",
                Long.class)
                .getSingleResult();
    }

    public Long getDriversWorkedEnough(int hours) {
        return sessionFactory.getCurrentSession().createQuery("SELECT COUNT(d) FROM Driver d WHERE d.hoursThisMonth=:hours",
                Long.class).setParameter("hours", hours).getSingleResult();
    }

    public User findUserByDriverId(Long id) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d.user FROM Driver d WHERE d.id=:id",
                User.class).setParameter("id", id).getSingleResult();
    }

}