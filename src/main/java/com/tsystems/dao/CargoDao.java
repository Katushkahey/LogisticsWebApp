package com.tsystems.dao;

import com.tsystems.configurations.HibernateSessionFactoryConfig;
import com.tsystems.entities.Cargo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDao {

    public Cargo findById(int id) {
        return HibernateSessionFactoryConfig.getSessionFactory().openSession().get(Cargo.class, id);
    }

    public void save(Cargo cargo) {
        Session session = HibernateSessionFactoryConfig.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(cargo);
        tx1.commit();
        session.close();
    }

    public void update(Cargo cargo) {
        Session session = HibernateSessionFactoryConfig.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(cargo);
        tx1.commit();
        session.close();
    }

    public void delete(Cargo cargo) {
        Session session = HibernateSessionFactoryConfig.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(cargo);
        tx1.commit();
        session.close();
    }
}
