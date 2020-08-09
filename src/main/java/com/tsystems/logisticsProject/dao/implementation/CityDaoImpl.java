package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.City;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CityDaoImpl {

    @Autowired
    Session session;

    public City findById(int id) {
        return session.get(City.class, id);
    }

    public void save(City city) {
        session.save(city);
    }

    public void update(City city) {
        session.update(city);
    }

    public void delete(City city) {
        session.delete(city);
    }
}

