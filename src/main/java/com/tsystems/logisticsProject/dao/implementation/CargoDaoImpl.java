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
    Session session;

    public Cargo findById(int id) {
        return  session.get(Cargo.class, id);
    }

    public void save(Cargo cargo) {
        session.save(cargo);
    }

    public void update(Cargo cargo) {
        session.update(cargo);
    }

    public void delete(Cargo cargo) {
        session.delete(cargo);
    }
}
