package com.tsystems.service;

import com.tsystems.dao.implementation.DriverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private DriverDao driverDao;

    @Autowired
    public void setDriverDao(DriverDao driverDao) {
        this.driverDao = driverDao;
    }
}
