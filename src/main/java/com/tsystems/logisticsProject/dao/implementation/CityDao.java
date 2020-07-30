package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CityDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional
    public City findById(int id) {
        return sessionFactory.openSession().get(City.class, id);
    }

    @Transactional
    public void save(City city) {
        Session session = sessionFactory.openSession();
        session.save(city);
    }

    @Transactional
    public void update(City city) {
        Session session = sessionFactory.openSession();
        session.update(city);
    }

    @Transactional
    public void delete(City city) {
        Session session = sessionFactory.openSession();
        session.delete(city);
    }
}

