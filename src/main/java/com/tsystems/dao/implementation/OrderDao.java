package com.tsystems.dao.implementation;

import com.tsystems.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Order findById(int id) {
        return sessionFactory.openSession().get(Order.class, id);
    }

    @Transactional
    public void save(Order order) {
        Session session = sessionFactory.openSession();
        session.save(order);
    }

    @Transactional
    public void update(Order order) {
        Session session = sessionFactory.openSession();
        session.update(order);
    }

    @Transactional
    public void delete(Order order) {
        Session session = sessionFactory.openSession();
        session.delete(order);
    }
}
