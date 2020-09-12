package com.tsystems.logisticsProject.dao.impl;

import com.tsystems.logisticsProject.dao.RoleDao;
import com.tsystems.logisticsProject.entity.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role findByAuthority(String authority) {
        return sessionFactory.getCurrentSession().createQuery("SELECT r FROM Role r WHERE r.authority=:authority", Role.class)
                .setParameter("authority", authority)
                .getSingleResult();
    }
}