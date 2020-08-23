package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.UserDao;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public User findByUsername(String username) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
