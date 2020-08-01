package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserDao implements com.tsystems.logisticsProject.dao.abstraction.UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        Session session = sessionFactory.openSession();
        session.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return  null;
    }

    @Override
    public User getById(Long id) {
        return sessionFactory.openSession().get(User.class, id);
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.openSession();
        session.update(user);
    }

    @Override
    public void remove(User user) {
        Session session = sessionFactory.openSession();
        session.delete(user);
    }

    public User getUserByUsername(String username) {
        return sessionFactory.openSession().get(User.class, username);
    }
}
