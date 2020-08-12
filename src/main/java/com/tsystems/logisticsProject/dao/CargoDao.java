package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Cargo;

public interface CargoDao extends GenericDao<Cargo> {

    void add(Cargo cargo);

    Cargo findById(Long id);

    void update(Cargo cargo);

    void delete(Cargo cargo);
}
