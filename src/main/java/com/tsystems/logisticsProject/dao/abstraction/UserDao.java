package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    List<User> getAllUsers();

    User getById(Long id);

    void update(User truck);

    void remove(User truck);
}
