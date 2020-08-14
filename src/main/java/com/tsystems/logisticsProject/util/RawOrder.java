package com.tsystems.logisticsProject.util;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.service.CityService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RawOrder {

    private Cargo cargo;
    Map<Cargo, Integer> mapOfCargoes;
    List<Cargo> listOfCargoes;
    private Waypoint waypoint;
    private List<Waypoint> listOfWaypoints;

    @Autowired
    private CityService cityService;


    @PostConstruct
    public void init() {
        mapOfCargoes = new HashMap<>();
        listOfCargoes = new ArrayList<>();
        listOfWaypoints = new ArrayList<>();

    }

    public void addNewCargo(String name, Double weight) {
        Cargo rawCargo = new Cargo();
        rawCargo.setName(name);
        rawCargo.setWeight(weight);
        if (listOfCargoes.size() == 0) {
            rawCargo.setId(1L);
        } else {
            rawCargo.setId(listOfCargoes.get(listOfCargoes.size() - 1).getId() + 1);
        }
        listOfCargoes.add(rawCargo);
    }

    public void deleteCargoById(Long id) {
        for (Cargo cargo : listOfCargoes) {
            if (cargo.getId() == id) {
                listOfCargoes.remove(cargo);
                break;
            }
        }
    }

    public void editCargo(Long id, String name, double weight) {
        for (Cargo cargo : listOfCargoes) {
            if (cargo.getId() == id) {
                cargo.setName(name);
                cargo.setWeight(weight);
                break;
            }
        }
    }

    public void saveCargoes() {
        for (Cargo cargo : listOfCargoes) {
            mapOfCargoes.put(cargo, 2);
        }
        listOfCargoes.clear();
    }

    public void addWaypoint(Long cargoId, String cityName) {

        Waypoint newWaypoint = new Waypoint();
        newWaypoint.setCity(cityService.findByCityName(cityName));
        if (listOfWaypoints.size() == 0) {
            newWaypoint.setId(1L);
        } else {
            newWaypoint.setId(listOfWaypoints.get(listOfWaypoints.size() - 1).getId() + 1);
        }

        Iterator it = mapOfCargoes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry cargoInfo = (Map.Entry) it.next();
            Cargo cargo = (Cargo) cargoInfo.getKey();
            if (cargo.getId() == cargoId) {
                newWaypoint.setCargo(cargo);
                if ((Integer) cargoInfo.getValue() == 2) {
                    newWaypoint.setAction(Action.LOADING);
                    cargoInfo.setValue(1);
                } else {
                    newWaypoint.setAction(Action.UNLOADING);
                    mapOfCargoes.remove(cargoInfo.getKey());
                }
                listOfWaypoints.add(newWaypoint);
                break;
            }
        }
    }

    public void deleteWaypointById(Long id) {
        for (Waypoint waypoint : listOfWaypoints) {
            if (waypoint.getId() == id) {
                Cargo cargoFromRemovedWaypoint = waypoint.getCargo();
                for (Waypoint waypoint1 : listOfWaypoints) {
                    if (waypoint.getCargo().getId() == cargoFromRemovedWaypoint.getId() && waypoint.getId() != id) {
                        listOfWaypoints.remove(waypoint1);
                        break;
                    }
                }
                listOfWaypoints.remove(waypoint);
                mapOfCargoes.put(cargoFromRemovedWaypoint, 2);
                break;
            }
        }

    }

    public void saveOrder() {
        // to do
    }

    public void clearAll() {
        mapOfCargoes.clear();
        listOfWaypoints.clear();
        listOfCargoes.clear();
    }
}
