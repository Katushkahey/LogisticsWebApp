package com.tsystems.logisticsProject.dao.abstraction;

import com.tsystems.logisticsProject.entity.Cargo;

import java.util.List;

public interface CargoDao extends GenericDao<Cargo> {

    void add(Cargo cargo);

    List<Cargo> getAllCargoes();

    Cargo getById(Long id);

    void update(Cargo cargo);

    void remove(Cargo cargo);
}
