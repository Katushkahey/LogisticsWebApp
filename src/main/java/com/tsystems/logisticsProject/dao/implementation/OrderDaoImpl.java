package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class OrderDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public Order findById(Long id) {
        return  sessionFactory.openSession().get(Order.class, id);
    }

    public void save(Order order) {
        sessionFactory.openSession().save(order);
    }

    public void update(Order order) {
        sessionFactory.openSession().update(order);
    }

    public void delete(Order order) {
        sessionFactory.openSession().delete(order);
    }
}
