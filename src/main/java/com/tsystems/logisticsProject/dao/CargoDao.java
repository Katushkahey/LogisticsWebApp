package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Cargo;

import java.util.List;

public interface CargoDao extends GenericDao<Cargo> {

    void add(Cargo cargo);

    Cargo findById(Long id);

    void update(Cargo cargo);

    void delete(Cargo cargo);
}
