package com.tsystems.logisticsProject.service.abstraction;

import com.tsystems.logisticsProject.entity.User;

public interface UserService {

    public User findByUsername(String username);

    public void add(User user, String authority);

}
