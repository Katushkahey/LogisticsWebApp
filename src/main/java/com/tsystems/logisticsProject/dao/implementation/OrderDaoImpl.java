package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Order findById(Long id) {
        return sessionFactory.getCurrentSession().get(Order.class, id);
    }

    public void add(Order order) {
        sessionFactory.getCurrentSession().merge(order);
    }

    public void update(Order order) {
        sessionFactory.getCurrentSession().update(order);
    }

    public List<Order> findCompetedOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status", Order.class)
                .setParameter("status", OrderStatus.COMPLETED)
                .getResultList();
    }

    public List<Order> findUnassignedOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status", Order.class)
                .setParameter("status", OrderStatus.NOT_ASSIGNED)
                .getResultList();
    }

    public List<Order> findWaitingOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status", Order.class)
                .setParameter("status", OrderStatus.WAITING)
                .getResultList();
    }

    public List<Order> findOrdersInProgress() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status", Order.class)
                .setParameter("status", OrderStatus.IN_PROGRESS)
                .getResultList();
    }

    public void delete(Order order) {
        sessionFactory.getCurrentSession().delete(order);
    }
}