package com.tsystems.dao;

import com.tsystems.configurations.HibernateSessionFactoryConfig;
import com.tsystems.entities.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DriverDao {

    private SessionFactory factory = HibernateSessionFactoryConfig.getSessionFactory();
    private CityDao cityDao;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public Driver findDriverById(Long id) {
        try (final Session session = factory.openSession()) {
            session.beginTransaction();
            final Driver driver = session.get(Driver.class, id);
            session.getTransaction().commit();
            return driver;
        }
    }

    public void addDriver(String name, String surname, String telephoneNumber, String city, Integer user_id) {
        try (final Session session = factory.openSession()) {
            session.beginTransaction();
            Long id = cityDao.findCityIdByName(city);
            Driver newDriver = new Driver(name, surname, telephoneNumber, id, user_id);
            session.save(newDriver);
            session.getTransaction().commit();
        }
    }

    /*
     * что бы сделать update, нужно сделать сначала find, а потом просто поменять Java-объект, воспользовавшись setter-ами.
     * написать все методы update по мере необходимости.
     */
    public void updateDriverById(Long id, Integer currentTruckId) {
        try (final Session session = factory.openSession()) {
            session.beginTransaction();
            Driver driver = session.get(Driver.class, id);
            driver.setCurrentTruckId(currentTruckId);
            session.update(driver);
            session.getTransaction().commit();
        }
    }

    public void deleteDriverById(Long id) {
        try (final Session session = factory.openSession()) {
            session.beginTransaction();
            Driver driver = session.get(Driver.class, id);
            session.delete(driver);
            session.getTransaction().commit();
        }
    }
}
