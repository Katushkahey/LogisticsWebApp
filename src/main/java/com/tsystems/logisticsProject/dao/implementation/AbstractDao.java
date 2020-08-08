package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.GenericDao;
import com.tsystems.logisticsProject.entity.AbstractEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    Session session;

    @Override
    public void add(T entity) {
        session.save(entity);
    }

    @Override
    public void update(T entity) {
        session.update(entity);
    }

    @Override
    public void delete(T entity) {
        session.delete(entity);
    }

}
