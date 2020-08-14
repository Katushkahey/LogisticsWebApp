package com.tsystems.logisticsProject.service.implementation;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.service.CargoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CargoServiceImpl implements CargoService {

    @Transactional
    public void addWaypoint(Cargo cargo, Waypoint waypoint) {
        if (cargo.getWaypoints() == null) {
            List<Waypoint> listOfWaypoints = new ArrayList<>();
            listOfWaypoints.add(waypoint);
            cargo.setWaypoints(listOfWaypoints);
        }
        cargo.getWaypoints().add(waypoint);
    }
}
