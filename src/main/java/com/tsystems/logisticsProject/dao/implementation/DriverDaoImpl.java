package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class DriverDaoImpl extends AbstractDao<Driver> implements DriverDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Driver findById(Long id) {
        return sessionFactory.getCurrentSession().get(Driver.class, id);
    }

    public void add(Driver driver) {
        sessionFactory.getCurrentSession().save(driver);
    }

    public void update(Driver driver) {
        sessionFactory.getCurrentSession().update(driver);
    }

    public void delete(Driver driver) {
        sessionFactory.getCurrentSession().delete(driver);
    }

    public Driver findByUser(User user) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.user=:user", Driver.class)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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

    public boolean checkEditedTelephoneNumber(String telephoneNumber, Long id) {
        try {
            sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE " +
                    "d.telephoneNumber=:telephoneNumber AND d.id<>:id", Driver.class).
                    setParameter("telephoneNumber", telephoneNumber)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public boolean findByTelephoneNubmer(String telephoneNumber) {
        try {
            sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE " +
                    "d.telephoneNumber=:telephoneNumber", Driver.class).
                    setParameter("telephoneNumber", telephoneNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public List<Driver> findDriversForTruck(City city, int maxSpentTimeForDriver) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.currentCity=:city" +
                    " AND d.hoursThisMonth<=:hours and d.currentOrder is null", Driver.class)
                    .setParameter("city", city)
                    .setParameter("hours", maxSpentTimeForDriver)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}