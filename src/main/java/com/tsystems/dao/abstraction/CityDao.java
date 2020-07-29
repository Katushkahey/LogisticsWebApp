package com.tsystems.dao.abstraction;

import com.tsystems.entity.City;

import java.util.List;

public interface CityDao {

    void add(City city);

    List<City> getAllCities();

    City getById(Long id);

    void update(City city);

    void remove(City city);
}
