package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.implementation.DriverDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private DriverDaoImpl driverDao;

    @Autowired
    public void setDriverDao(DriverDaoImpl driverDao) {
        this.driverDao = driverDao;
    }
}
