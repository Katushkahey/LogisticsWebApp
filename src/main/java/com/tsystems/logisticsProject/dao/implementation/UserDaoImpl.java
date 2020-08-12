package com.tsystems.logisticsProject.dao.implementation;

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

    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    public void update(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    public void delete(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        User user;
        try {
            user = sessionFactory.getCurrentSession().createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

}
