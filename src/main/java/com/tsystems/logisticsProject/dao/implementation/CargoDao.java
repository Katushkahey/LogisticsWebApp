package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CargoDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional
    public Cargo findById(int id) {
        return sessionFactory.openSession().get(Cargo.class, id);
    }

    @Transactional
    public void save(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.save(cargo);
    }

    @Transactional
    public void update(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.update(cargo);
    }

    @Transactional
    public void delete(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.delete(cargo);
    }
}
