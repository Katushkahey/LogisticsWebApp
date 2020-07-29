package com.tsystems.dao.abstraction;

import com.tsystems.entity.Cargo;

import java.util.List;

public interface CargoDao {

    void add(Cargo cargo);

    List<Cargo> getAllCargoes();

    Cargo getById(Long id);

    void update(Cargo cargo);

    void remove(Cargo cargo);
}
