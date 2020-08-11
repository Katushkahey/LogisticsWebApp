package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.User;

public interface UserDao extends GenericDao<User> {

    void add(User user);

    void update(User user);

    User findByUsername(String username);
}
