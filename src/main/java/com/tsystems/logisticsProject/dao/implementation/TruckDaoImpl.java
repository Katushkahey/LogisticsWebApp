package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TruckDaoImpl extends AbstractDao<Truck> implements TruckDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Truck findById(Long id) {
        return sessionFactory.getCurrentSession().get(Truck.class, id);
    }

    public void add(Truck truck) {
        sessionFactory.getCurrentSession().save(truck);
    }

    public void update(Truck truck) {
        sessionFactory.getCurrentSession().update(truck);
    }

    public void delete(Truck truck) {
        sessionFactory.getCurrentSession().delete(truck);
    }

    public List<Truck> findAll() {
        return sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t order by t.id", Truck.class)
                .getResultList();
    }

    public boolean findByNumber(String number) {
        try {
            sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t WHERE t.number=:number", Truck.class)
                    .setParameter("number", number)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public boolean checkEditedNumber(String number, Long id) {
        try {
            sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t WHERE t.number=:number AND " +
                    "t.id<>:id", Truck.class).setParameter("number", number)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public List<Truck> findTrucksForOrder(double maxOneTimeWeight) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM Truck t WHERE t.capacity>=:weight AND t.truckState=:state "+
                            "AND t.id NOT IN (SELECT o.orderTruck FROM Order o where o.orderTruck is not null)", Truck.class)
                    .setParameter("weight", maxOneTimeWeight)
                    .setParameter("state", TruckState.OK)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}


