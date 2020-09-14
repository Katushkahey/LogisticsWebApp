package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    void add(String username, String authority);

}
