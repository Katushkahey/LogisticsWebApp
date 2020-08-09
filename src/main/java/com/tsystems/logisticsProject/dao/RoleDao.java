package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Role;

public interface RoleDao extends GenericDao<Role> {

    public Role findByAuthority(String authority);
}
