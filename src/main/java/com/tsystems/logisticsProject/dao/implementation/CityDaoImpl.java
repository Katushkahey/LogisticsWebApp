package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CityDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public City findById(int id) {
        return  sessionFactory.openSession().get(City.class, id);
    }

    public void save(City city) {
        sessionFactory.openSession().save(city);
    }

    public void update(City city) {
        sessionFactory.openSession().update(city);
    }

    public void delete(City city) {
        sessionFactory.openSession().delete(city);
    }
}

