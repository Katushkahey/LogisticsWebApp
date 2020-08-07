package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CargoDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public Cargo findById(int id) {
        return  sessionFactory.openSession().get(Cargo.class, id);
    }

    public void save(Cargo cargo) {
        sessionFactory.openSession().save(cargo);
    }

    public void update(Cargo cargo) {
        sessionFactory.openSession().update(cargo);
    }

    public void delete(Cargo cargo) {
        sessionFactory.openSession().delete(cargo);
    }
}
