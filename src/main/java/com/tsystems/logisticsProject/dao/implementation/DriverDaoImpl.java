package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DriverDaoImpl implements DriverDao {

    @Autowired
    Session session;

    public Driver findById(Long id) {
        return  session.get(Driver.class, id);
    }

    public void add(Driver driver) {
        session.save(driver);
    }

    public void update(Driver driver) {
        session.update(driver);
    }

    public void delete(Driver driver) {
        session.delete(driver);
        session.flush();
    }

    public Driver findByUser(User user) {
        return session.createQuery("SELECT d FROM Driver d WHERE d.user=:user", Driver.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Driver> findAll() {
        return session.createQuery("SELECT d FROM Driver d", Driver.class)
                .getResultList();
    }

}
