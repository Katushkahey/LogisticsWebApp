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

    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setDependencies(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
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
    public void add(User user) {
        userDao.add(user);
    }

    @Transactional
    public void add(User user, String authority) {
        User newUser = findByUsername(user.getUsername());

        if (newUser != null) {
            return;
        }

        user.setAuthority(roleDao.findByAuthority(authority));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        add(user);
    }

    @Transactional
    public User returnUserToCreateDriver(String userName) {
        User userToReturn = findByUsername(userName);
        if (userToReturn == null) {
            User newDriver = new User();
            newDriver.setUsername(userName);
            newDriver.setPassword("driver");
            add(newDriver, "ROLE_DRIVER");
            return newDriver;
        } else {
            return userToReturn;
        }
    }

    @Transactional
    public boolean checkUserNameToCreateDriver(String userName) {
        if (findByUsername(userName) == null) {
            return false;
        }
        return true;
    }
}