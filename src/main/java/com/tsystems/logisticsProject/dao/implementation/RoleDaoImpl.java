package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.RoleDao;
import com.tsystems.logisticsProject.entity.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Role findByAuthority(String authority) {
        if (authority == null) {
            return null;
        }
        return  sessionFactory.getCurrentSession().createQuery("SELECT r FROM Role r WHERE r.authority=:authority", Role.class)
                .setParameter("authority", authority)
                .getSingleResult();
    }
}
