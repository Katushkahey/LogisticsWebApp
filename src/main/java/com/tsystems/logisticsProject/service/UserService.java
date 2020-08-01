package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.dao.implementation.UserDaoImpl;
import com.tsystems.logisticsProject.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserService {

    private UserDaoImpl userDao;

    @Autowired
    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public User findByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

}
