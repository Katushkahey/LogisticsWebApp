package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DriverDaoImpl extends AbstractDao<Driver> implements DriverDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Driver findById(Long id) {
        return  sessionFactory.getCurrentSession().get(Driver.class, id);
    }

    public void add(Driver driver) {
        sessionFactory.getCurrentSession().save(driver);
    }

    public void update(Driver driver) {
        sessionFactory.getCurrentSession().update(driver);
    }

    public void delete(Driver driver) {
        sessionFactory.getCurrentSession().delete(driver);
//        session.flush();
    }

    public Driver findByUser(User user) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.user=:user", Driver.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Driver> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d", Driver.class)
                .getResultList();
    }

    public List<Driver> findAllDriversForCurrentOrder(Order order) {
        return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Driver d WHERE d.currentOrder=:order", Driver.class)
                .setParameter("order", order)
                .getResultList();
    }

}
