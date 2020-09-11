package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {

    private SessionFactory sessionFactory;

    @Autowired
    public  void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Order findById(Long id) {
        return sessionFactory.getCurrentSession().get(Order.class, id);
    }

    public List<Order> findCompetedOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status order by o.id", Order.class)
                .setParameter("status", OrderStatus.COMPLETED)
                .getResultList();
    }

    public List<Order> findUnassignedOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status order by o.id", Order.class)
                .setParameter("status", OrderStatus.NOT_ASSIGNED)
                .getResultList();
    }

    public List<Order> findWaitingOrders() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status order by o.id", Order.class)
                .setParameter("status", OrderStatus.WAITING)
                .getResultList();
    }

    public List<Order> findOrdersInProgress() {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.status=:status order by o.id", Order.class)
                .setParameter("status", OrderStatus.IN_PROGRESS)
                .getResultList();
    }

    public Order findByNumber(String number) {
        return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.number=:number", Order.class)
                .setParameter("number", number)
                .getSingleResult();
    }

    public Order findByTruck(Truck truck) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o WHERE o.orderTruck=:truck",
                    Order.class).setParameter("truck", truck).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Order> getTopOrders(int number) {
        return  sessionFactory.getCurrentSession().createQuery("SELECT o FROM Order o order by o.id", Order.class)
                .setMaxResults(number)
                .getResultList();
    }

//    select truck_id from orders where id = 28

    public Truck findTruckOfOrder(Long id) {
        return sessionFactory.getCurrentSession().createQuery("SELECT o.orderTruck from Order o where o.id=:id",
                Truck.class).setParameter("id", id).getSingleResult();
    }

}