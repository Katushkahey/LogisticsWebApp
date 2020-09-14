package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.RoleDao;
import com.tsystems.logisticsProject.dao.UserDao;
import com.tsystems.logisticsProject.entity.User;
import com.tsystems.logisticsProject.service.UserService;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setDependencies(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Transactional
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    public void add(String username, String authority) {
        User user = new User();
        user.setUsername(username);
        user.setAuthority(roleDao.findByAuthority(authority));
        user.setPassword(passwordEncoder.encode("driver"));
        userDao.add(user);
    }

}