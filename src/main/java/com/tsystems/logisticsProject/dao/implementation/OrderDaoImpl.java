package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Order;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class OrderDaoImpl {

    @Autowired
    SessionFactory sessionFactory;

    public Order findById(Long id) {
        return sessionFactory.openSession().get(Order.class, id);
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

    public List<Order> findCompetedOrders() {
        return sessionFactory.openSession().createQuery("SELECT o FROM Order o WHERE o.isCompleted = true", Order.class)
                .getResultList();
    }

    /**
     * select*from orders where is_completed is false
     * and truck_id is null
     * union
     * select*from orders where is_completed is false
     * and id not in (select current_order_id from drivers);
     */
    public List<Order> findUnassignedOrders() {
        return sessionFactory.openSession().createQuery("SELECT o FROM Order o WHERE o.isCompleted = false " +
                "AND o.orderTruck IS NULL OR (o.isCompleted = false AND o.id NOT IN (SELECT d.currentOrder FROM Driver d))", Order.class)
                .getResultList();
    }

    /**
     * select*from orders where is_completed is false and truck_id is not null
     * and id in (select current_order_id from drivers);
     */
    public List<Order> findAssignedOrders() {
        return sessionFactory.openSession().createQuery("SELECT o FROM Order o WHERE o.isCompleted = false " +
                "AND o.orderTruck IS NOT NULL AND o.id IN (SELECT d.currentOrder FROM Driver d)", Order.class)
                .getResultList();
    }
}
