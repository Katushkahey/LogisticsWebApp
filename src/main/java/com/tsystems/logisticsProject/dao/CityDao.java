package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.City;

import java.util.List;

public interface CityDao extends GenericDao<City> {

    List<City> findAll();

    City findById(Long id);

    boolean containsCityName(String name);
}
