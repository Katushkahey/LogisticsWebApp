package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Transactional
    public List<City> getListOfCities() {
        return cityDao.findAll();
    }

    @Transactional
    public City findByCityName(String name) {
        return cityDao.findByName(name);
    }
}
