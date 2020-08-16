package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoServiceImpl implements CargoService {

    @Autowired
    CargoDao cargoDao;

    @Transactional
    public void delete(Cargo cargo) {
        cargoDao.delete(cargo);
    }
}
