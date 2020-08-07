package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.abstraction.RoleDao;
import com.tsystems.logisticsProject.dao.abstraction.UserDao;
import com.tsystems.logisticsProject.dao.implementation.UserDaoImpl;
import com.tsystems.logisticsProject.entity.User;
import com.tsystems.logisticsProject.service.abstraction.UserService;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@NoArgsConstructor
@EnableTransactionManagement
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    Session session;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

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