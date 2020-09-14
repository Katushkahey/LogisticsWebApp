package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.User;

public interface UserDao extends GenericDao<User> {

    User findByUsername(String username);
}
