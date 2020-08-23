package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.entity.Cargo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDaoImpl extends AbstractDao<Cargo>  implements CargoDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Cargo findByNumber(String number) {
        return sessionFactory.openSession().createQuery("SELECT c FROM Cargo c WHERE c.number=:number", Cargo.class)
                .setParameter("number", number)
                .getSingleResult();
    }

}
