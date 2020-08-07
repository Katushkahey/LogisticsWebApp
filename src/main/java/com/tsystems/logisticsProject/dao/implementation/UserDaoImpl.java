package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.UserDao;
import com.tsystems.logisticsProject.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@EnableTransactionManagement
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    @Autowired
    SessionFactory sessionFactory;

    public void add(User user) {
        sessionFactory.openSession().save(user);
    }

    public void update(User user) {
        sessionFactory.openSession().update(user);
    }

    public void delete(User user) {
        sessionFactory.openSession().delete(user);
    }

    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        return  sessionFactory.openSession().createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
