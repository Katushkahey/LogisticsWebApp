package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CargoDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public Cargo findById(int id) {
        return sessionFactory.openSession().get(Cargo.class, id);
    }

    public void save(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.save(cargo);
    }

    public void update(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.update(cargo);
    }

    public void delete(Cargo cargo) {
        Session session = sessionFactory.openSession();
        session.delete(cargo);
    }
}
