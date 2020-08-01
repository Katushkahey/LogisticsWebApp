package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public void add(User user) {
        Session session = sessionFactory.openSession();
        session.save(user);
    }


    public void update(User user) {
        Session session = sessionFactory.openSession();
        session.update(user);
    }

    public void remove(User user) {
        Session session = sessionFactory.openSession();
        session.delete(user);
    }

    public User getUserByUsername(String username) {
        return sessionFactory.openSession().get(User.class, 2L);
    }
}
