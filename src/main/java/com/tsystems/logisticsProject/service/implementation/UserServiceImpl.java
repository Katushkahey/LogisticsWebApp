package com.tsystems.logisticsProject.service.implementation;

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

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username);
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
    public void add(User user, String authority) {
        User newUser = userDao.findByUsername(user.getUsername());

        if (newUser != null) {
            return;
        }

        user.setAuthority(roleDao.findByAuthority(authority));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.add(user);
    }
}