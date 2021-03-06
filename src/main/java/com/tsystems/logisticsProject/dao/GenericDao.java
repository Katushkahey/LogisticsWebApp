package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.AbstractEntity;

public interface GenericDao<T extends AbstractEntity> {

    void add(T entity);

    void update(T entity);

    void delete(T entity);
}