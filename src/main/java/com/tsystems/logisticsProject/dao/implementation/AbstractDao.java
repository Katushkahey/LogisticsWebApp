package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.GenericDao;
import com.tsystems.logisticsProject.entity.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(T entity) {
        sessionFactory.getCurrentSession().save(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

}
