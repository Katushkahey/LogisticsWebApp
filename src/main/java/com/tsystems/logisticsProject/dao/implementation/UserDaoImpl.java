package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.UserDao;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    @Autowired
    Session session;

    public void add(User user) {
        session.save(user);
    }

    public void update(User user) {
       session.update(user);
    }

    public void delete(User user) {
        session.delete(user);
    }

    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        return session.createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
