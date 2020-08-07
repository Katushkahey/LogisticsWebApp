package com.tsystems.logisticsProject.dao.implementation;

import com.tsystems.logisticsProject.dao.abstraction.RoleDao;
import com.tsystems.logisticsProject.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Role findByAuthority(String authority) {
        if (authority == null) {
            return null;
        }
        return  sessionFactory.openSession().createQuery("SELECT r FROM Role r WHERE r.authority=:authority", Role.class)
                .setParameter("authority", authority)
                .getSingleResult();
    }
}
