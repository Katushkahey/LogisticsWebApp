package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.Truck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class TruckDaoImpl {

    @Autowired
    Session session;

    public Truck findById(Long id) {
        return session.get(Truck.class, id);
    }

    public void save(Truck truck) {
        session.save(truck);
    }

    public void update(Truck truck) {
        session.update(truck);
    }

    public void delete(Truck truck) {
        session.delete(truck);
    }

    public List<Truck> findAll() {
        return session.createQuery("SELECT t FROM Truck t", Truck.class)
                .getResultList();
    }
}
