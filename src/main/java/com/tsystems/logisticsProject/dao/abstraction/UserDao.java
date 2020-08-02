package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.User;

public interface UserDao extends GenericDao<User> {

    public Long add(User user);

    public void update(User user);

    public void delete(User user);

    public User getUserByUsername(String username);
}
