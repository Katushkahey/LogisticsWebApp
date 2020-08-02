package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.GenericDao;
import com.tsystems.logisticsProject.entity.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Long add(T entity) {
        sessionFactory.openSession().save(entity);
        return entity.getId();
    }

    @Override
    public void update(T entity) {
        sessionFactory.openSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.openSession().delete(entity);
    }
}
