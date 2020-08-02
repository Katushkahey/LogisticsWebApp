package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.abstraction.UserDao;
import com.tsystems.logisticsProject.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserServiceImpl  {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setDependencies(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.add(user);
    }

}
