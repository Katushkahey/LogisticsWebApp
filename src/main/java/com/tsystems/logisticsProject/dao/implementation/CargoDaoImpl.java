package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDaoImpl implements CargoDao {

    @Autowired
    Session session;

    public Cargo findById(Long id) {
        return  session.get(Cargo.class, id);
    }

    public void add(Cargo cargo) {
        session.save(cargo);
    }

    public void update(Cargo cargo) {
        session.update(cargo);
    }

    public void delete(Cargo cargo) {
        session.delete(cargo);
    }
}
