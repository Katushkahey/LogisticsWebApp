package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.entity.City;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CityDaoImpl extends AbstractDao<City> implements CityDao {

    @Autowired
    private SessionFactory sessionFactory;

    public City findById(Long id) {
        return sessionFactory.getCurrentSession().get(City.class, id);
    }

    public void add(City city) {
        sessionFactory.getCurrentSession().save(city);
    }

    public void update(City city) {
        sessionFactory.getCurrentSession().update(city);
    }

    public void delete(City city) {
        sessionFactory.getCurrentSession().delete(city);
    }
}

