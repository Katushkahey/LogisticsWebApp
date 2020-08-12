package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.City;

import java.util.List;

public interface CityDao extends GenericDao<City> {

    List<City> findAll();

    City findByName(String name);

}
