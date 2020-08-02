package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.AbstractEntity;

public interface GenericDao<T extends AbstractEntity> {

    Long add(T entity);

    void update(T entity);

    void delete(T entity);

}
