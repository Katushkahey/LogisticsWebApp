package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.City;

import java.util.List;

public interface CityDao {

    void add(City city);

    List<City> getAllCities();

    City getById(Long id);

    void update(City city);

    void remove(City city);
}
