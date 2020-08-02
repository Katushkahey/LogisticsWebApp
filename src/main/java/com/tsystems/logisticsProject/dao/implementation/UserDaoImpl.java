package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.UserDao;
import com.tsystems.logisticsProject.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    public Long add(User user) {
        sessionFactory.openSession().save(user);
        return user.getId();
    }

    public void update(User user) {
       sessionFactory.openSession().update(user);
    }

    public void delete(User user) {
        sessionFactory.openSession().delete(user);
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        return sessionFactory.openSession().createQuery("SELECT d FROM User d WHERE d.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();

    }
}
