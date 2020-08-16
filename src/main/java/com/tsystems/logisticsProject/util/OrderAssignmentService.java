package com.tsystems.logisticsProject.util;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.util.entity.CombinationForOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Component
public class OrderAssignmentService {

    private final int AVERAGE_VELOCITY = 60;
    private final int TIME_OF_ORDER_FOR_ONE_DRIVER_MAX = 12;
    private final int TIME_OF_ORDER_FOR_TWO_DRIVERS_MAX = 48;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static int fullDaysToEndMonth;
    private static int hoursToEndMonth;
    private List<CombinationForOrder> listOfCombinationForOrder;
    private long combinationId;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TruckService truckService;

    @Autowired
    private DriverService driverService;

    public List<CombinationForOrder> createListOfCombinationsForOrder(Long orderId) {
        listOfCombinationForOrder = new ArrayList<>();
        combinationId = 0;
        Map<City, List<Truck>> mapOfTrucksForEveryCity = getMapOfTrucksForEveryCity(orderId);
        Set<City> setOfCity = mapOfTrucksForEveryCity.keySet();
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(setOfCity, orderId);
        Map<City, Integer> mapOfHoursForOrderFromEveryCity = calculateTimeForOrderFromEveryCity(setOfCity, orderId);

        for (City city: setOfCity) {
            List<Truck> listOfTruckForCurrentOrderInEveryCity = mapOfTrucksForEveryCity.get(city);
            int optionalMaxDriversForOrderFromThisCity = mapOfMaxOptionalNumberOfDrivers.get(city);
            int hoursForOrderFromThisCity = mapOfHoursForOrderFromEveryCity.get(city);
//            int requiredNumberOfHoursForOrder = calculateRequiredNumberOfHours(hoursForOrderFromThisCity);
            for (Truck truck: listOfTruckForCurrentOrderInEveryCity) {
                // это значит что вместимость фуры 2, а нужно 3 человека, значит едем по 16 часов в днеь.
                if (truck.getCrewSize() < optionalMaxDriversForOrderFromThisCity) {
                    int requiredNumberOfDaysToCompleteOrder = (int)Math.ceil(hoursForOrderFromThisCity / 16);
                    int requiredNumberOfHoursPerPerson;
                    if (requiredNumberOfDaysToCompleteOrder > fullDaysToEndMonth) {
                        requiredNumberOfHoursPerPerson = fullDaysToEndMonth * 8;
                    } else {
                        requiredNumberOfHoursPerPerson = (int) Math.ceil(hoursForOrderFromThisCity / 2);
                    }
                    int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursPerPerson);
                    List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                    if(listOfDrivers != null) {

                        //  значит мы нашли только 1 подходящего водителя, теперь поищем кого-то с меньшим, но подходящим кол-вом свободных часов.
                        if (listOfDrivers.size() < truck.getCrewSize()) {
                            int requiredNumberOfDaysToCompleteOrder2 = (int) Math.ceil(hoursForOrderFromThisCity / 8);
                            int requiredNumberOfHoursPerPerson2;
                            if (requiredNumberOfDaysToCompleteOrder2 > fullDaysToEndMonth) {
                                requiredNumberOfHoursPerPerson2 = fullDaysToEndMonth * 8;
                            } else {
                                requiredNumberOfHoursPerPerson2 = hoursForOrderFromThisCity;
                            }
                            int maxSpentTimeForDriver2 = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursPerPerson2);
                            List<Driver> listOfDrivers2 = driverService.findDriversForTruck(city, maxSpentTimeForDriver2);
                            if (listOfDrivers2 != null) {
                                List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers2, 1);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination);
                            }
                        } else if (listOfDrivers.size() == truck.getCrewSize()) {
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);
                        } else {
                            List<Driver> listOfDribersForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 2);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDribersForCombination);
                        }
                    }
                } else {
                    // если необходимое число водителей для выполнения заказа == 1, значит заказ максимум на 12 часов.
                    if (optionalMaxDriversForOrderFromThisCity == 1) {
                        int requiredNumberOfHoursToSelectDriver;
                        if(hoursToEndMonth < hoursForOrderFromThisCity) {
                            requiredNumberOfHoursToSelectDriver = hoursToEndMonth;
                        } else {
                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                        }
                        int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null) {
                            returnDriversWithMinValidTimeAsList(listOfDrivers, 1);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);
                        }
                        //  значит заказ от 12 до 24 часов
                    } else if (optionalMaxDriversForOrderFromThisCity == 2) {
                        int requiredNumberOfHoursToSelectDriver;
                        if(hoursToEndMonth < hoursForOrderFromThisCity) {
                            requiredNumberOfHoursToSelectDriver = hoursToEndMonth;
                        } else {
                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                        }
                        int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null) {

                            // значит на такое кол-во времени нашелся только 1 водитель, на выполнение заказа времени ему понадобится больше,
                            // проверим, хватит ли ему времени в таком случа
                            if (listOfDrivers.size() < 2) {
                                int newTimeToCompleteOrder = hoursForOrderFromThisCity * 2;
                                if(hoursToEndMonth < hoursForOrderFromThisCity) {
                                    requiredNumberOfHoursToSelectDriver = hoursToEndMonth;
                                } else {
                                    requiredNumberOfHoursToSelectDriver = newTimeToCompleteOrder;
                                }
                                int newMaxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);

                                //если ему хватает часов на выполнение данного заказа
                                if(listOfDrivers.get(0).getHoursThisMonth() >= newMaxSpentTimeForDriver) {
                                    addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);
                                }
                            } else if (listOfDrivers.size() == 2) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);
                            } else  {
                                List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 2);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination);
                            }
                        }
                        // раз нам нужно 3 человека, значит заказ длительный, едем по 8 часов каждый
                    } else {
                        int requiredNumberOfHoursToSelectDriver;
                        int numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 24;
                        if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                            requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                        } else {
                            requiredNumberOfHoursToSelectDriver = numberOfDaysToCompleteOrder * 8;
                        }
                        int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null) {
                            if(listOfDrivers.size() > 3) {
                                List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 3);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination);
                            } else if (listOfDrivers.size() == 3) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);

                                //значит мы нашли либо 2 либо 1 водителя
                            } else {
                                if (listOfDrivers.size() == 1) {
                                    numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 8;
                                    if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                        requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                    } else {
                                        requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                                    }
                                    maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                    if (listOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers);
                                    }
                                } else {
                                    numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 16;
                                    if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                        requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                    } else {
                                        requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity / 2;
                                    }
                                    maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                    List<Driver> newListOfDrivers = new ArrayList<>();
                                    for (Driver driver: listOfDrivers) {
                                        if(driver.getHoursThisMonth() < maxSpentTimeForDriver) {
                                            newListOfDrivers.add(driver);
                                        }
                                    }
                                    if (newListOfDrivers.size() == 2) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers);
                                    } else if (newListOfDrivers.size() == 1) {
                                        numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 8;
                                        if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                            requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                        } else {
                                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                                        }
                                        maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                        if (newListOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return listOfCombinationForOrder;
    }

    private void addCombinationForOrderInList(Truck truck, int hoursForOrderFromThisCity, List<Driver> listOfDriversForCombination) {
        combinationId ++;
        CombinationForOrder combinationForOrder = new CombinationForOrder();
        combinationForOrder.setId(combinationId);
        combinationForOrder.setListOfDrivers(listOfDriversForCombination);
        combinationForOrder.setTruck(truck);
        combinationForOrder.setTotalHours(hoursForOrderFromThisCity * 24 / (8 * listOfDriversForCombination.size()));
        combinationForOrder.setTotalBillableHours(hoursForOrderFromThisCity);
        listOfCombinationForOrder.add(combinationForOrder);
    }

    private int calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(int requiredNumberOfHoursPerPerson) {
        return Driver.MAX_HOURS_IN_MONTH - requiredNumberOfHoursPerPerson;
    }

    private List<Driver> returnDriversWithMinValidTimeAsList(List<Driver> listOfDrivers, int numberOfDrivers) {
        long minHours = 0;
        List<Driver> listOfDriversToReturn = new ArrayList<>();
        while(numberOfDrivers > 0) {
            Driver driverToReturn = null;
            for (Driver driver : listOfDrivers) {
                long validHours = driver.MAX_HOURS_IN_MONTH - driver.getHoursThisMonth();
                if (validHours < minHours) {
                    minHours = validHours;
                    driverToReturn = driver;
                }
            }
            listOfDriversToReturn.add(driverToReturn);
            listOfDrivers.remove(driverToReturn);
            numberOfDrivers --;
        }
        return listOfDriversToReturn;
    }

    private static void calculateLeftTime() {
        int year = YearMonth.now().getYear();
        int month = YearMonth.now().getMonthValue();

        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        int today = LocalDate.now().getDayOfMonth();

        int hour = LocalDateTime.now().getHour();
        fullDaysToEndMonth = daysInMonth - today;
        int hoursInMonth = daysInMonth * 24;
        int hoursFromStartMonth = (today - 1) * 24 + hour;
        hoursToEndMonth = hoursInMonth - hoursFromStartMonth;
    }

    private Map<City, List<Truck>> getMapOfTrucksForEveryCity(Long orderId) {
        Map<City, List<Truck>> mapOfTrucksForEveryCity = new HashMap<>();
        List<Truck> listOfTruckForOrder = getListOfTruckForOrder(orderId);
        Set<City> setOfCities = getSetOfCitiesFromListOfTrucksForOrder(orderId);

        for(City city: setOfCities) {
            List<Truck> listOfTrucksForCity = new ArrayList<>();
            for (Truck truck: listOfTruckForOrder) {
                if (truck.getCurrentCity().getId() == city.getId()) {
                    listOfTrucksForCity.add(truck);
                }
            }
            mapOfTrucksForEveryCity.put(city, listOfTrucksForCity);
        }
        return mapOfTrucksForEveryCity;
    }

    private Set<City> getSetOfCitiesFromListOfTrucksForOrder(Long orderId) {
        List<Truck> listOfTrucksForOrder = getListOfTruckForOrder(orderId);
        return getSetOfCitiesFromListOfTrucks(listOfTrucksForOrder);
    }

    private List<Truck> getListOfTruckForOrder(Long orderId) {
        double maxWeightForOrder = calculateMaxOneTimeWeightForOrder(orderId) / 1000;
        return truckService.findTrucksForOrder(maxWeightForOrder);
    }

    private Map<City, Integer> calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(Set<City> listOfCities, Long orderId) {
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = new HashMap<>();
        Map<City, Integer> mapOfHoursForEveryStartCity = calculateTimeForOrderFromEveryCity(listOfCities, orderId);
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

    private Map<City, Integer> calculateTimeForOrderFromEveryCity(Set<City> setOfCities, Long orderId) {
        int hoursForLoadingUnloading = calculateHoursForLoadingUnloading(orderId);
        Map<City, Integer> mapOfHoursForEveryStartCity = new HashMap<>();;
        for (City city : setOfCities) {
            int hoursForDriving = calculateHoursForDrivingByDistance(calculateDistanceForOrderDependingOnTruckCity(city, orderId));
            int totalalNumberOfHours = hoursForDriving + hoursForLoadingUnloading;
            mapOfHoursForEveryStartCity.put(city, totalalNumberOfHours);
        }
        return mapOfHoursForEveryStartCity;
    }

    private double calculateDistanceForOrderDependingOnTruckCity(City city, Long orderId) {
        List<Waypoint> listOfWaypoints = getListOfWaypointsForCurrentOrderById(orderId);
        City startCity = listOfWaypoints.get(0).getCity();
        City endCity = listOfWaypoints.get(listOfWaypoints.size() - 1).getCity();
        double distanceToFirstWaypoint = calculateDistanceBetweenTwoCities(city, startCity);
        double distanceFromLastWaypointToHome = calculateDistanceBetweenTwoCities(endCity, city);
        return distanceToFirstWaypoint + calculateDistanceBetweenWaypointsForOrderById(orderId) + distanceFromLastWaypointToHome;
    }

    private Set<City> getSetOfCitiesFromListOfTrucks(List<Truck> listOfTruck) {
        Set<City> setOfCities = new HashSet<>();
        for (Truck truck : listOfTruck) {
            setOfCities.add(truck.getCurrentCity());
        }
        return setOfCities;
    }

    private double calculateMaxOneTimeWeightForOrder(Long orderId) {
        return orderService.getMaxWeightDuringTheRouteOfCurrentOrderById(orderId);
    }

    private int calculateHoursForDrivingByDistance(Double distance) {
        return (int) Math.ceil(distance / AVERAGE_VELOCITY);
    }

    private int calculateHoursForLoadingUnloading(Long orderId) {
        return getListOfWaypointsForCurrentOrderById(orderId).size() / 2;
    }

//    private Double calculateDistanceBetweenWaypointsForOrderById(Long id) {
//        Waypoint waypointFrom;
//        Waypoint waypointTo;
//        double distanceBetweenWaypointsOfOrder = 0;
//        Waypoint[] arrayOfWaypoints = (Waypoint[]) getListOfWaypointsForCurrentOrderById(id).toArray();
//
//        for (int i = 0; i < arrayOfWaypoints.length - 1; i++) {
//            waypointFrom = arrayOfWaypoints[i];
//            waypointTo = arrayOfWaypoints[i + 1];
//            double distanceBetweenCurrentWaypoints = calculateDistanceBetweenTwoCities(waypointFrom.getCity(), waypointTo.getCity());
//            distanceBetweenWaypointsOfOrder += distanceBetweenCurrentWaypoints;
//        }
//        return distanceBetweenWaypointsOfOrder;
//    }

    private Double calculateDistanceBetweenWaypointsForOrderById(Long id) {
        Waypoint waypointFrom;
        Waypoint waypointTo;
        double distanceBetweenWaypointsOfOrder = 0;
        List<Waypoint> listOfWaypoint = getListOfWaypointsForCurrentOrderById(id);

        int i = 0;
        while (i < listOfWaypoint.size() - 1) {
            waypointFrom = listOfWaypoint.get(i);
            waypointTo = listOfWaypoint.get(i + 1);
            double distanceBetweenCurrentWaypoints = calculateDistanceBetweenTwoCities(waypointFrom.getCity(), waypointTo.getCity());
            distanceBetweenWaypointsOfOrder += distanceBetweenCurrentWaypoints;
            i++;
        }
        return distanceBetweenWaypointsOfOrder;
    }

    private List<Waypoint> getListOfWaypointsForCurrentOrderById(Long id) {
        return orderService.findWaypointsForCurrentOrderById(id);
    }

    private Double calculateDistanceBetweenTwoCities(City cityFrom, City cityTo) {
        return Math.sqrt(Math.pow((cityTo.getLat() - cityFrom.getLat()), 2) +
                Math.pow((cityTo.getLng() - cityFrom.getLng()), 2));
    }

}
