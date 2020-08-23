package com.tsystems.logisticsProject.dao;

import com.tsystems.logisticsProject.entity.Cargo;

public interface CargoDao extends GenericDao<Cargo> {

    Cargo findByNumber(String number);
}
