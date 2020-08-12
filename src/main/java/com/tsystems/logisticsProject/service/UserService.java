package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    void add(User user, String authority);

    User returnUserToCreateDriver(String userName);

    boolean checkUserNameToCreateDriver(String userName);

}
