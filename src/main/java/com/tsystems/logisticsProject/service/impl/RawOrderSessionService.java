package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import com.tsystems.logisticsProject.service.CityService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
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
public class RawOrderSessionService {

    @Autowired
    private TruckService truckService;

    private Cargo cargo;
    Map<Cargo, Integer> mapOfCargoes;
    List<Cargo> listOfCargoes;
    private Waypoint waypoint;
    private List<Waypoint> listOfWaypoints;

    @Autowired
    private CityService cityService;

    @Autowired
    private OrderService orderService;

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
    }

    public void addWaypoint(Long cargoId, String cityName) {

        Waypoint newWaypoint = new Waypoint();
        newWaypoint.setCity(cityService.findByCityName(cityName));
        if (listOfWaypoints.size() == 0) {
            newWaypoint.setId(1L);
        } else {
            newWaypoint.setId(listOfWaypoints.get(listOfWaypoints.size() - 1).getId() + 1);
        }

        for (Cargo cargo : listOfCargoes) {
            if (cargo.getId() == cargoId) {
                addWaypoint(cargo, newWaypoint);
            }
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

    private void addWaypoint(Cargo cargo, Waypoint waypoint) {
        if (cargo.getWaypoints() == null) {
            List<Waypoint> listOfWaypoints = new ArrayList<>();
            listOfWaypoints.add(waypoint);
            cargo.setWaypoints(listOfWaypoints);
        }
        cargo.getWaypoints().add(waypoint);
    }

    public void editWaypoint(Long id, String cityName) {
        City newCityForWaypoint = cityService.findByCityName(cityName);

        editWaypointInListOfWaypoints(id, newCityForWaypoint);
        editWaypointInListOfCargoes(id, newCityForWaypoint);
    }

    private void editWaypointInListOfWaypoints(Long id, City city) {
        for (Waypoint waypoint : listOfWaypoints) {
            if (waypoint.getId() == id) {
                waypoint.setCity(city);
            }
        }
    }

    private void editWaypointInListOfCargoes(Long id, City city) {
        for (Cargo cargo : listOfCargoes) {
            List<Waypoint> waypoints = cargo.getWaypoints();
            for (Waypoint waypoint : waypoints) {
                if (waypoint.getId() == id) {
                    waypoint.setCity(city);
                }
            }
        }
    }

    public void deleteWaypointById(Long id) {
        for (Waypoint waypoint : listOfWaypoints) {
            if (waypoint.getId() == id) {
                Cargo cargoFromRemovedWaypoint = waypoint.getCargo();
                if (waypoint.getAction().equals(Action.LOADING)) {
                    for (Waypoint waypoint1 : listOfWaypoints) {
                        if (waypoint1.getCargo().getId() == cargoFromRemovedWaypoint.getId() && waypoint1.getId() != id) {
                            listOfWaypoints.remove(waypoint1);
                            break;
                        }
                    }
                    listOfWaypoints.remove(waypoint);
                    mapOfCargoes.put(cargoFromRemovedWaypoint, 2);
                    break;
                } else {
                    listOfWaypoints.remove(waypoint);
                    mapOfCargoes.put(cargoFromRemovedWaypoint, 1);
                    break;
                }
            }
        }
        deleteWaypointFromItsCargoById(id);
    }

    private void deleteWaypointFromItsCargoById(Long id) {
        for (Cargo cargo : listOfCargoes) {
            if (cargo.getWaypoints() != null) {
                List<Waypoint> listOfWaypoint = cargo.getWaypoints();
                for (Waypoint waypoint : listOfWaypoint) {
                    if (waypoint.getId() == id) {
                        listOfWaypoint.remove(waypoint);
                        break;
                    }
                }
            }
        }
    }

    public void saveOrder(String number) {
        for (Waypoint waypoint : listOfWaypoints) {
            waypoint.setSequence(waypoint.getId());
            waypoint.setId(null);
            waypoint.setStatus(WaypointStatus.TODO);
            waypoint.getCargo().setId(null);

        }
        Order order = new Order();
        order.setCargoes(listOfCargoes);
        order.setStatus(OrderStatus.NOT_ASSIGNED);
        order.setNumber(number);
        for (Cargo cargo : listOfCargoes) {
            cargo.setOrder(order);
        }
        orderService.add(order);
        clearAll();
    }

    public void clearAll() {
        mapOfCargoes.clear();
        listOfWaypoints.clear();
        listOfCargoes.clear();
    }

    /**
     * this method can be used for debugging
     */
    private void printHashMap() {
        Iterator it = mapOfCargoes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry cargoInfo = (Map.Entry) it.next();
            System.out.println("" + cargoInfo.getKey() + " " + cargoInfo.getValue());
        }
    }

    public boolean checkMaxWeightOfOrder() {
        double maxWeight = 0;
        double totalWeight = 0;
        for (Waypoint waypoint : listOfWaypoints) {
            if (waypoint.getAction().equals(Action.LOADING)) {
                totalWeight += waypoint.getCargo().getWeight();
                if (maxWeight < totalWeight) {
                    maxWeight = totalWeight;
                }
            } else {
                totalWeight -= waypoint.getCargo().getWeight();
            }
        }
        if (maxWeight / 1000 > truckService.getMaxCapacity()) {
            return false;
        }
        return true;
    }
}
