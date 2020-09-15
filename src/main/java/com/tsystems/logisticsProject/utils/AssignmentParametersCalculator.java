package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.exception.unchecked.EntityNotFoundException;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.util.*;

@Component
public class AssignmentParametersCalculator {

    private final int TIME_OF_ORDER_FOR_ONE_DRIVER_MAX = 12;
    private final int TIME_OF_ORDER_FOR_TWO_DRIVERS_MAX = 48;

    private TimeCalculator timeCalculator;
    private OrderService orderService;
    private TruckService truckService;

    @Autowired
    public void setDependencies(TimeCalculator timeCalculator, OrderService orderService,
                                TruckService truckService) {
        this.timeCalculator = timeCalculator;
        this.orderService = orderService;
        this.truckService = truckService;
    }

    /**
     * select required number drivers from total list with min valid working time in this month in order
     *  to leave drivers with biger valid number of working hours for longer orders. This allows
     *  for more optimal usage of resources.
     * @return list of drivers with min valid time
     */
    public List<Driver> returnDriversWithMinValidTimeAsList(List<Driver> listOfDrivers, int numberOfDrivers) {
        List<Driver> listOfDriversToReturn = new ArrayList<>();
        while (numberOfDrivers > 0) {
            long minHours = 200;
            Driver driverToReturn = null;
            for (Driver driver : listOfDrivers) {
                long validHours = Driver.MAX_HOURS_IN_MONTH - driver.getHoursThisMonth();
                if (validHours <= minHours) {
                    minHours = validHours;
                    driverToReturn = driver;
                }
            }
            listOfDriversToReturn.add(driverToReturn);
            listOfDrivers.remove(driverToReturn);
            numberOfDrivers--;
        }
        return listOfDriversToReturn;
    }

    public Map<City, List<Truck>> getMapOfTrucksForEveryCity(Long orderId) {
        Map<City, List<Truck>> mapOfTrucksForEveryCity = new HashMap<>();
        List<Truck> listOfTruckForOrder = getListOfTruckForOrder(orderId);
        Set<City> setOfCities = getSetOfCitiesFromListOfTrucks(listOfTruckForOrder);
        for (City city : setOfCities) {
            List<Truck> listOfTrucksForCity = new ArrayList<>();
            for (Truck truck : listOfTruckForOrder) {
                if (truck.getCurrentCity().getId() == city.getId()) {
                    listOfTrucksForCity.add(truck);
                }
            }
            mapOfTrucksForEveryCity.put(city, listOfTrucksForCity);
        }
        return mapOfTrucksForEveryCity;
    }

    public Set<City> getSetOfCitiesFromListOfTrucks(List<Truck> listOfTruck) {
        Set<City> setOfCities = new HashSet<>();
        for (Truck truck : listOfTruck) {
            setOfCities.add(truck.getCurrentCity());
        }
        return setOfCities;
    }

    /**
     * calculate required number of drivers to complete order depending on calculated total hours
     * to complete order.
     * @return recommended number of drivers for this order
     */
    public Map<City, Integer> calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(Set<City> listOfCities, Long orderId) {
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = new HashMap<>();
        Map<City, Integer> mapOfHoursForEveryStartCity = timeCalculator.calculateTimeForOrderFromEveryCity(listOfCities,
                orderId);
        for (City city : listOfCities) {
            int hours = mapOfHoursForEveryStartCity.get(city);
            if (hours <= TIME_OF_ORDER_FOR_ONE_DRIVER_MAX) {
                mapOfMaxOptionalNumberOfDrivers.put(city, 1);
            } else if (hours <= TIME_OF_ORDER_FOR_TWO_DRIVERS_MAX) {
                mapOfMaxOptionalNumberOfDrivers.put(city, 2);
            } else {
                mapOfMaxOptionalNumberOfDrivers.put(city, 3);
            }
        }
        return mapOfMaxOptionalNumberOfDrivers;
    }


    /**
     * find all trucks that are not broken and whose capacity is suitable for this order
     * @return list of suitable trucks
     */
    public List<Truck> getListOfTruckForOrder(Long orderId) {
        List<Waypoint> listOfWaypoints = orderService.findWaypointsForCurrentOrderById(orderId);
        double maxWeightForOrder = orderService.getMaxWeightForOrderById(listOfWaypoints) / 1000;
        try {
            return truckService.findTrucksForOrder(maxWeightForOrder);
        } catch (NoResultException e) {
            throw new EntityNotFoundException("weight " + maxWeightForOrder, Truck.class);
        }
    }

}
