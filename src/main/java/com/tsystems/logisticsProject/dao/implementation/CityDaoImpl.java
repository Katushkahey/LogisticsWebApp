package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.entity.City;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityDaoImpl extends AbstractDao<City> implements CityDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void add(City city) {
        sessionFactory.getCurrentSession().save(city);
    }

    public void update(City city) {
        sessionFactory.getCurrentSession().update(city);
    }

    public void delete(City city) {
        sessionFactory.getCurrentSession().delete(city);
    }

    public List<City> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT c FROM City c", City.class)
                .getResultList();
    }

    public City findByName(String name) {
        return sessionFactory.getCurrentSession().createQuery("SELECT c FROM City c WHERE c.name=:name", City.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}