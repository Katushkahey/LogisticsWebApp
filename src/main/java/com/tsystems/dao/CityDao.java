package com.tsystems.dao;

import com.tsystems.configurations.HibernateSessionFactoryConfig;
import com.tsystems.entities.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityDao {

    private SessionFactory factory = HibernateSessionFactoryConfig.getSessionFactory();

    public Long findCityIdByName(String name) {
        City city;
        try (final Session session = factory.openSession()) {
            session.beginTransaction();
            city = session.get(City.class, name);
            session.getTransaction().commit();
        }
        return city.getId();
    }
}

