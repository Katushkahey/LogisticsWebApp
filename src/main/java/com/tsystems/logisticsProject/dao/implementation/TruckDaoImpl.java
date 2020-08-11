package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TruckDaoImpl implements TruckDao {

    @Autowired
    Session session;

    @Autowired
    OrderDaoImpl orderDaoImpl;

    public Truck findById(Long id) {
        return session.get(Truck.class, id);
    }

    public void add(Truck truck) {
        session.save(truck);
    }

    public void update(Truck truck) {
        session.update(truck);
        session.flush();
    }

    public void delete(Truck truck) {
        session.delete(truck);
    }

    public List<Truck> findAll() {
        return session.createQuery("SELECT t FROM Truck t", Truck.class)
                .getResultList();
    }

    public boolean findByNumber(String number) {
        try {
            session.createQuery("SELECT t FROM Truck t WHERE t.number=:number", Truck.class)
                    .setParameter("number", number)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }
}
