package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDaoImpl extends AbstractDao<Cargo> implements CargoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Cargo findById(Long id) {
        return sessionFactory.getCurrentSession().get(Cargo.class, id);
    }

    public void add(Cargo cargo) {
        sessionFactory.getCurrentSession().save(cargo);
    }

    public void update(Cargo cargo) {
        sessionFactory.getCurrentSession().update(cargo);
    }

    public void delete(Cargo cargo) {
        sessionFactory.getCurrentSession().delete(cargo);
    }
}
