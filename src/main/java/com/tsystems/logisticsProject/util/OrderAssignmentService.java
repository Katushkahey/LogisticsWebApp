//package com.tsystems.logisticsProject.util;
//
//import com.tsystems.logisticsProject.entity.City;
//import com.tsystems.logisticsProject.entity.Driver;
//import com.tsystems.logisticsProject.entity.Truck;
//import com.tsystems.logisticsProject.entity.Waypoint;
//import com.tsystems.logisticsProject.service.DriverService;
//import com.tsystems.logisticsProject.service.OrderService;
//import com.tsystems.logisticsProject.service.TruckService;
//import com.tsystems.logisticsProject.util.entity.CombinationForOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.YearMonth;
//import java.util.*;
//
//@Component
//public class OrderAssignmentService {
//
//    private final int AVERAGE_VELOCITY = 60;
//    private final int TIME_OF_ORDER_FOR_ONE_DRIVER_MAX = 12;
//    private final int TIME_OF_ORDER_FOR_TWO_DRIVERS_MAX = 48;
//    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//    private static int fullDaysToEndMonth;
//    private static int hoursToEndMonth;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private TruckService truckService;
//
//    @Autowired
//    private DriverService driverService;
//
//    public List<CombinationForOrder> createListOfCombinationsForOrder(Long orderId) {
//        List<CombinationForOrder> listOfCombinationForOrder = new ArrayList<>();
//        Map<City, List<Truck>> mapOfTrucksForEveryCity = getMapOfTrucksForEveryCity(orderId);
//        Set<City> setOfCity = mapOfTrucksForEveryCity.keySet();
//        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(setOfCity, orderId);
//        Map<City, Integer> mapOfHoursForOrderFromEveryCity = calculateTimeForOrderFromEveryCity(setOfCity, orderId);
//
//        for (City city: setOfCity) {
//            List<Truck> listOfTruckForCurrentOrderInEveryCity = mapOfTrucksForEveryCity.get(city);
//            int optionalMaxDriversForOrderFromThisCity = mapOfMaxOptionalNumberOfDrivers.get(city);
//            int hoursForOrderFromThisCity = mapOfHoursForOrderFromEveryCity.get(city);
////            int requiredNumberOfHoursForOrder = calculateRequiredNumberOfHours(hoursForOrderFromThisCity);
//            for (Truck truck: listOfTruckForCurrentOrderInEveryCity) {
//                // это значит что вместимость фуры 2, а нужно 3 человека, значит едем по 16 часов в днеь.
//                if (truck.getCrewSize() < optionalMaxDriversForOrderFromThisCity) {
//                    int requiredNumberOfDaysToCompleteOrder = (int)Math.ceil(hoursForOrderFromThisCity/16);
//                    if (requiredNumberOfDaysToCompleteOrder > fullDaysToEndMonth) {
//                        int requiredNumberOfHoursPerPerson = fullDaysToEndMonth * 8;
//                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, requiredNumberOfHoursPerPerson);
//                        if(listOfDrivers != null) {
//                            //  значит мы нашли только 1 подходящего водителя, теперь поищем кого-то с меньшим, но подходящим кол-вом свободных часов.
//                            if (listOfDrivers.size() < truck.getCrewSize()) {
//                                int requiredNumberOfDaysToCompleteOrder2 = (int) Math.ceil(hoursForOrderFromThisCity / 8);
//                                int requiredNumberOfHoursPerPerson2;
//                                if (requiredNumberOfDaysToCompleteOrder2 > fullDaysToEndMonth) {
//                                    requiredNumberOfHoursPerPerson2 = fullDaysToEndMonth * 8;
//                                } else {
//                                    requiredNumberOfHoursPerPerson2 = hoursForOrderFromThisCity;
//                                }
//                                List<Driver> listOfDrivers2 = driverService.findDriversForTruck(city, requiredNumberOfHoursPerPerson2);
//                                if (listOfDrivers2 != null) {
//                                    CombinationForOrder combinationForOrder = new CombinationForOrder();
//                                    combinationForOrder.setListOfDrivers(returnDriverWithMinValidTimeAsList(listOfDrivers2));
//                                    combinationForOrder.setTruck(truck);
//                                    combinationForOrder.setTotalHours((hoursForOrderFromThisCity/8)*24);
//                                    combinationForOrder.setTotalBillableHours(hoursForOrderFromThisCity);
//                                    listOfCombinationForOrder.add(combinationForOrder);
//                                }
//                            } else {
//
//                            }
//                        }
//                    }
////                    List<Driver> listOfDriversForThisTruck = driverService.findDriversForTruck(truck.getCurrentCity(),
////                            requiredNumberOfHours);
//                } else {
////                    int requiredNumberOfHoursForOrderPerPerson = requiredNumberOfHoursForOrder/optionalMaxDriversForOrderFromThisCity;
//                }
//            }
//        }
//        return listOfCombinationForOrder;
//    }
//
//    private List<Driver> returnDriverWithMinValidTimeAsList(List<Driver> listOfDrivers) {
//        long minHours = 0;
//        List<Driver> listOfDriversToReturn = new ArrayList<>();
//        Driver driverToReturn = null;
//        for(Driver driver: listOfDrivers) {
//            long validHours = driver.MAX_HOURS_IN_MONTH - driver.getHoursThisMonth();
//            if (validHours < minHours) {
//                minHours = validHours;
//                driverToReturn = driver;
//            }
//        }
//        listOfDrivers.add(driverToReturn);
//        return listOfDrivers;
//    }
//
//    private int calculateRequiredNumberOfHours(int hoursForOrderFromThisCity) {
//        if (hoursForOrderFromThisCity < hoursToEndMonth) {
//            return hoursForOrderFromThisCity;
//        } else {
//            return hoursToEndMonth;
//        }
//    }
//
//    private static void calculateLeftTime() {
//        int year = YearMonth.now().getYear();
//        int month = YearMonth.now().getMonthValue();
//
//        YearMonth yearMonthObject = YearMonth.of(year, month);
//        int daysInMonth = yearMonthObject.lengthOfMonth();
//
//        int today = LocalDate.now().getDayOfMonth();
//
//        int hour = LocalDateTime.now().getHour();
//        fullDaysToEndMonth = daysInMonth - today;
//        int hoursInMonth = daysInMonth * 24;
//        int hoursFromStartMonth = (today - 1) * 24 + hour;
//        hoursToEndMonth = hoursInMonth - hoursFromStartMonth;
//    }
//
//    private Map<City, List<Truck>> getMapOfTrucksForEveryCity(Long orderId) {
//        Map<City, List<Truck>> mapOfTrucksForEveryCity = new HashMap<>();
//        List<Truck> listOfTruckForOrder = getListOfTruckForOrder(orderId);
//        Set<City> setOfCities = getSetOfCitiesFromListOfTrucksForOrder(orderId);
//
//        for(City city: setOfCities) {
//            List<Truck> listOfTrucksForCity = new ArrayList<>();
//            for (Truck truck: listOfTruckForOrder) {
//                if (truck.getCurrentCity().getId() == city.getId()) {
//                    listOfTrucksForCity.add(truck);
//                }
//            }
//            mapOfTrucksForEveryCity.put(city, listOfTrucksForCity);
//        }
//        return mapOfTrucksForEveryCity;
//    }
//
//    private Set<City> getSetOfCitiesFromListOfTrucksForOrder(Long orderId) {
//        List<Truck> listOfTrucksForOrder = getListOfTruckForOrder(orderId);
//        return getSetOfCitiesFromListOfTrucks(listOfTrucksForOrder);
//    }
//
//    private List<Truck> getListOfTruckForOrder(Long orderId) {
//        double maxWeightForOrder = calculateMaxOneTimeWeightForOrder(orderId);
//        return truckService.findTrucksForOrder(maxWeightForOrder);
//    }
//
//    private Map<City, Integer> calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(Set<City> listOfCities, Long orderId) {
//        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = new HashMap<>();
//        Map<City, Integer> mapOfHoursForEveryStartCity = calculateTimeForOrderFromEveryCity(listOfCities, orderId);
//        for (City city : listOfCities) {
//            int hours = mapOfHoursForEveryStartCity.get(city);
//            if (hours <= TIME_OF_ORDER_FOR_ONE_DRIVER_MAX) {
//                mapOfMaxOptionalNumberOfDrivers.put(city, 1);
//            } else if (hours <= TIME_OF_ORDER_FOR_TWO_DRIVERS_MAX) {
//                mapOfMaxOptionalNumberOfDrivers.put(city, 2);
//            } else {
//                mapOfMaxOptionalNumberOfDrivers.put(city, 3);
//            }
//        }
//        return mapOfMaxOptionalNumberOfDrivers;
//    }
//
//
//    private Map<City, Integer> calculateTimeForOrderFromEveryCity(Set<City> setOfCities, Long orderId) {
//        int hoursForLoadingUnloading = calculateHoursForLoadingUnloading(orderId);
//        Map<City, Integer> mapOfHoursForEveryStartCity = new HashMap<>();;
//        for (City city : setOfCities) {
//            int hoursForDriving = calculateHoursForDrivingByDistance(calculateDistanceForOrderDependingOnTruckCity(city, orderId));
//            int totalalNumberOfHours = hoursForDriving + hoursForLoadingUnloading;
//            mapOfHoursForEveryStartCity.put(city, totalalNumberOfHours);
//        }
//        return mapOfHoursForEveryStartCity;
//    }
//
//    private double calculateDistanceForOrderDependingOnTruckCity(City city, Long orderId) {
//        List<Waypoint> listOfWaypoints = getListOfWaypointsForCurrentOrderById(orderId);
//        City startCity = listOfWaypoints.get(0).getCity();
//        City endCity = listOfWaypoints.get(listOfWaypoints.size() - 1).getCity();
//        double distanceToFirstWaypoint = calculateDistanceBetweenTwoCities(city, startCity);
//        double distanceFromLastWaypointToHome = calculateDistanceBetweenTwoCities(endCity, city);
//        return distanceToFirstWaypoint + calculateDistanceBetweenWaypointsForOrderById(orderId) + distanceFromLastWaypointToHome;
//    }
//
//    private Set<City> getSetOfCitiesFromListOfTrucks(List<Truck> listOfTruck) {
//        Set<City> setOfCities = new HashSet<>();
//        for (Truck truck : listOfTruck) {
//            setOfCities.add(truck.getCurrentCity());
//        }
//        return setOfCities;
//    }
//
////    private Set<Integer> getSetOfCrewSizesFromListOfTrucks(Long orderId) {
////        Set<Integer> setOfCrewSizesFromListOfTrucks = new HashSet<>();
////        List<Truck> listOfTrucks = getListOfTruckForOrder(orderId);
////        for (Truck truck : listOfTrucks) {
////            setOfCrewSizesFromListOfTrucks.add(truck.getCrewSize());
////        }
////        return setOfCrewSizesFromListOfTrucks;
////    }
//
//    private double calculateMaxOneTimeWeightForOrder(Long orderId) {
//        return orderService.getMaxWeightDuringTheRouteOfCurrentOrderById(orderId);
//    }
//
//    private int calculateHoursForDrivingByDistance(Double distance) {
//        return (int) Math.ceil(distance / AVERAGE_VELOCITY);
//    }
//
//    private int calculateHoursForLoadingUnloading(Long orderId) {
//        return getListOfWaypointsForCurrentOrderById(orderId).size() / 2;
//    }
//
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
//
//    private List<Waypoint> getListOfWaypointsForCurrentOrderById(Long id) {
//        return orderService.findWaypointsForCurrentOrderById(id);
//    }
//
//    private Double calculateDistanceBetweenTwoCities(City cityFrom, City cityTo) {
//        return Math.sqrt(Math.pow((cityTo.getLat() - cityFrom.getLat()), 2) +
//                Math.pow((cityTo.getLng() - cityFrom.getLng()), 2));
//    }
//
//}
