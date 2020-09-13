package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.GenericDao;
import com.tsystems.logisticsProject.entity.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(T entity) {
        sessionFactory.getCurrentSession().save(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

}
