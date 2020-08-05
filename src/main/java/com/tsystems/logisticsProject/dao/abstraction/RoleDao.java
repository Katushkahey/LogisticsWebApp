package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.Role;

public interface RoleDao extends GenericDao<Role> {

    public Role findByAuthority(String authority);
}
