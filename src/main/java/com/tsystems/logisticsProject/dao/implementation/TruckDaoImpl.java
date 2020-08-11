package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TruckDaoImpl extends AbstractDao<Truck> implements TruckDao {

    @Autowired
    private SessionFactory sessionFactory;

//    @Autowired
//    private OrderDao orderDao;

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
        return sessionFactory.getCurrentSession().createQuery("SELECT t FROM Truck t", Truck.class)
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
}
