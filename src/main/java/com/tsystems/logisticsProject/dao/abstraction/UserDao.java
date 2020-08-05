package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.User;

public interface UserDao extends GenericDao<User> {

    public void add(User user);

    public void update(User user);

    public User findByUsername(String username);
}
