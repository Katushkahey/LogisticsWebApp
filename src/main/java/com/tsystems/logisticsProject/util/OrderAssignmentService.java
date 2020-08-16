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
        calculateLeftTime();
        Map<City, List<Truck>> mapOfTrucksForEveryCity = getMapOfTrucksForEveryCity(orderId);
        Set<City> setOfCity = mapOfTrucksForEveryCity.keySet();
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(setOfCity, orderId);
        Map<City, Integer> mapOfHoursForOrderFromEveryCity = calculateTimeForOrderFromEveryCity(setOfCity, orderId);

        // заходим в каждый город, где есть подходящая для выполнения заказа фура
        for (City city: setOfCity) {

            // получаем список фур, которые подходят для выполнения заказа в этом городе
            List<Truck> listOfTruckForCurrentOrderInEveryCity = mapOfTrucksForEveryCity.get(city);

            // получаем оптимальное количество водителей, необходимое для выполнения этого заказа
            int optionalMaxDriversForOrderFromThisCity = mapOfMaxOptionalNumberOfDrivers.get(city);

            // получаем количество часов, необходимое для выполнения данного заказа из этого города
            // с учетом расстояния от города старта и возвращеня домой
            int hoursForOrderFromThisCity = mapOfHoursForOrderFromEveryCity.get(city);

            //заходим в каждую фуру и подбираем ей список водителей, если таковые найдутся
            for (Truck truck: listOfTruckForCurrentOrderInEveryCity) {

                /** т.к вместимость фуры может быть либо 2 либо 3, а на заказ может потребоваться максимум 3 человека
                 * если вместимость фуры, меньше, чем требуемое количество человек,
                 * значит вместимость фуры 2, на заказ требуется 3 человека. 3 человека требуются только на длительные заказы,
                 * где каждый из водителей работает по 8 часов в день.
                 * Т.к фура вмещает только двоих, значит едем по 16 часов в день.
                 */
                if (truck.getCrewSize() < optionalMaxDriversForOrderFromThisCity) {
                    // количество дней на выполнение заказа = полное количество часов в пути / 16 часов в день ,округляем в большую сторону
                    int requiredNumberOfDaysToCompleteOrder = (int)Math.ceil(hoursForOrderFromThisCity / 16);
                    int requiredNumberOfHoursPerPerson;
                    /** если необходимое кол-во дней на выполнение заказа больше ,чем кол-во дней до конца месяца,
                    * то для поиска водителей время будет равно кол-ву дней до конца месяца * на 8 часов каждый день,
                    * тк после этого их часы обнулятся.
                    * Иначе, время для поиска водителя = полное время, необходимое на выполнение заказа/2, тк выполнят его пополам.
                    */
                    if (requiredNumberOfDaysToCompleteOrder > fullDaysToEndMonth) {
                        requiredNumberOfHoursPerPerson = fullDaysToEndMonth * 8;
                    } else {
                        requiredNumberOfHoursPerPerson = (int) Math.ceil(hoursForOrderFromThisCity / 2);
                    }
                    /** считаем максимальное количество часов ,которое может уже быть потрачено водителем на момент назначения заказа, что бы
                     * ему хватило времени на выполнение заказа
                     */
                    int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursPerPerson);
                    /** осуществляем запрос, который отдас нам водителей, у которых сейчас нет назначенного заказа, которые находятся в том же городе,
                     * что и фура и у которых хватает часов на выполнение заказа.
                      */
                    List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                    if(listOfDrivers != null && listOfDrivers.size() != 0) {

                        /** Т.к. вместимость фуры 2, как мы выяснили ранее ,а количество подходящих водителей меньше,
                         * значит мы нашли только 1 подходящего водителя, но он будет ехать все так же по 8 часов в день, тк заказ дальний
                         * таким, образом он будет ехать дольше, но такое же количество времени в день. Значит ему понадобится
                         * больше времени на выполнение заказа, если только месяц не закончится раньше. Пересчитаем необходимое количество времени еще раз
                         * и посмотрим, подходит ли нам все еще этот водителью
                         */
                        if (listOfDrivers.size() < truck.getCrewSize()) {
                            // необходимое кол-во дней на выполнение заказа = количество часов на выполнение заказа / 8 часов в день
                            int requiredNumberOfDaysToCompleteOrder2 = (int) Math.ceil(hoursForOrderFromThisCity / 8);
                            int requiredNumberOfHoursPerPerson2;
                            if (requiredNumberOfDaysToCompleteOrder2 > fullDaysToEndMonth) {
                                requiredNumberOfHoursPerPerson2 = fullDaysToEndMonth * 8;
                            } else {
                                requiredNumberOfHoursPerPerson2 = hoursForOrderFromThisCity;
                            }
                            int maxSpentTimeForDriver2 = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursPerPerson2);
                            // если у него хватит часов на выполнение заказа, то мы добавляем такой вариант в список возможных вариантов.
                            if (listOfDrivers.get(0).getHoursThisMonth() <= maxSpentTimeForDriver2) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                            }
                            // если количество найденных водителей = вместимости фуры, то просто добавим такой вариант в список возможных вариантов.
                        } else if (listOfDrivers.size() == truck.getCrewSize()) {
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                        } else {
                            /**если количество найденных водителей больше, чем вмещает в себя фура, мы из представленного списка выберем тех,
                             * у кого до конца месяца осталось наименьше количество часов, что бы оптимальнее расходовать ресурсы и добавим данную комбинацию в
                             * возможных комбинаций.
                              */
                            List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 2);

                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
                        }
                    }
                } else {
                    /**если необходимое число водителей для выполнения заказа == 1, значит заказ максимум на 12 часов ,
                     * значит один водитель будет выполнять весь заказ, и ему необходимо иметь полное кол-во часов на выполнение заказа
                      */
                    if (optionalMaxDriversForOrderFromThisCity == 1) {
                        int requiredNumberOfHoursToSelectDriver;
                        if(hoursToEndMonth < hoursForOrderFromThisCity) {
                            requiredNumberOfHoursToSelectDriver = hoursToEndMonth;
                        } else {
                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                        }
                        int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        System.out.println(listOfDrivers.toString());
                        /** если мы нашли людей, подходящих под это  заказ, то из них выберем того, у кого до конца месяца осталось наименьшее кол-во
                         * свободных часов, что бы оптимальнее расходовать ресурсы.
                         */
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            List<Driver> listOfDriversToCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 1);
                            System.out.println(listOfDriversToCombination);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversToCombination, optionalMaxDriversForOrderFromThisCity);
                        }
                        /**
                         *   если оптимальное количество человек для выполнения заказа = 2, значит заказ от 24 до 48 часов, значит мы предполагаем ,что
                         *   водители будут работать по 12 часов в день, тк заказ при таком расклады выходит максимум на 2 дня.
                         */
                    } else if (optionalMaxDriversForOrderFromThisCity == 2) {
                        int requiredNumberOfHoursToSelectDriver;
                        if(hoursToEndMonth < hoursForOrderFromThisCity) {
                            requiredNumberOfHoursToSelectDriver = hoursToEndMonth / 2;
                        } else {
                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity / 2;
                        }
                        int maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            /** если количество водителей в найденом списке, меньше 2 необходимых, значит
                             * з на такое кол-во времени нашелся только 1 водитель, а значит на выполнение заказа времени ему понадобится вдвое больше,
                             *  проверим, хватит ли ему времени в таком случае.
                             */
                            if (listOfDrivers.size() < 2) {
                                if(hoursToEndMonth < hoursForOrderFromThisCity) {
                                    requiredNumberOfHoursToSelectDriver = hoursToEndMonth;
                                } else {
                                    requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                                }
                                int newMaxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);

                                // если ему хватает часов на выполнение данного заказа, то мы добавляем данную комбинацию в список комбинаций
                                if(listOfDrivers.get(0).getHoursThisMonth() >= newMaxSpentTimeForDriver) {
                                    addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                }
                                // если список найденных нами водителей = 2, значит мы просто добавим такую комбинацию в список всех комбинаций
                            } else if (listOfDrivers.size() == 2) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                /**
                                 * если количество найденных водителей больше ,чем нам необходимо, мы просто выберем из всех тех, у кого до конца месяца осталось наименьшее
                                 * количество часов, что бы оптимальнее использовать ресурсы и добавим такую комбинацию в список всех возможных комбинаций.
                                 */
                            } else  {
                                List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 2);

                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
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
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            // если мы нашли больше водителей, чем нам нужно ,выберем 3 с наименьшим кол-вом оставшегося времени....
                            if(listOfDrivers.size() > 3) {
                                List<Driver> listOfDriversForCombination = returnDriversWithMinValidTimeAsList(listOfDrivers, 3);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);

                            } else if (listOfDrivers.size() == 3) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);

                                // если список подходящих водителей меньше ,чем нам нужно, значит мы нашли либо 2 либо 1 водителя
                            } else {
                                if (listOfDrivers.size() == 1) {
                                    numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 8;
                                    if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                        requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                    } else {
                                        requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                                    }
                                    maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                    // проверим ,хватит ли найденному водителю времени после перерасчета и если да, добавим комбинацию.
                                    if (listOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                    }
                                } else {
                                    numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 16;
                                    if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                        requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                    } else {
                                        requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity / 2;
                                    }
                                    maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                   // проверим хватит ли двум найденным водителя времени на заказ после перерасчета необходимого времени
                                    List<Driver> newListOfDrivers = new ArrayList<>();
                                    for (Driver driver: listOfDrivers) {
                                        if(driver.getHoursThisMonth() < maxSpentTimeForDriver) {
                                            newListOfDrivers.add(driver);
                                        }
                                    }
                                    // если в новом списке осталось 2 водителя, значит им обоим хватило времени, и мы добавляем комбинацию.
                                    if (newListOfDrivers.size() == 2) {

                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                        /**если в новом списке остался только 1 водитель, значит времени на выполнение заказа после перерасчета хватило только ему
                                         * но тк заказ он будет в таком случае выполнять 1, нам нужно снова перерасчитать время и убедиться, что водителю его хватит.
                                         */
                                    } else if (newListOfDrivers.size() == 1) {
                                        numberOfDaysToCompleteOrder = hoursForOrderFromThisCity / 8;
                                        if(fullDaysToEndMonth < numberOfDaysToCompleteOrder) {
                                            requiredNumberOfHoursToSelectDriver = fullDaysToEndMonth * 8;
                                        } else {
                                            requiredNumberOfHoursToSelectDriver = hoursForOrderFromThisCity;
                                        }
                                        maxSpentTimeForDriver = calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(requiredNumberOfHoursToSelectDriver);
                                        if (newListOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers, optionalMaxDriversForOrderFromThisCity);
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

    private void addCombinationForOrderInList(Truck truck, int hoursForOrderFromThisCity, List<Driver> listOfDriversForCombination, int optionalMaxDriversForOrderFromThisCity) {
        combinationId ++;
        CombinationForOrder combinationForOrder = new CombinationForOrder();
        combinationForOrder.setId(combinationId);
        combinationForOrder.setListOfDrivers(listOfDriversForCombination);
        combinationForOrder.setTruck(truck);
        if (optionalMaxDriversForOrderFromThisCity == 1) {
            combinationForOrder.setTotalHours(hoursForOrderFromThisCity);
        } else if (optionalMaxDriversForOrderFromThisCity == 2) {
            combinationForOrder.setTotalHours(hoursForOrderFromThisCity * 24 / (8 * listOfDriversForCombination.size()));
        } else {
            combinationForOrder.setTotalHours(hoursForOrderFromThisCity * 24 / (8 * listOfDriversForCombination.size()));
        }
        combinationForOrder.setTotalBillableHours(hoursForOrderFromThisCity);
        listOfCombinationForOrder.add(combinationForOrder);
    }

    private int calculateMaxSpentTimeForDriverFromRequiredNumberOfHoursPerPerson(int requiredNumberOfHoursPerPerson) {
        return Driver.MAX_HOURS_IN_MONTH - requiredNumberOfHoursPerPerson;
    }

    private List<Driver> returnDriversWithMinValidTimeAsList(List<Driver> listOfDrivers, int numberOfDrivers) {
        long minHours = 200;
        List<Driver> listOfDriversToReturn = new ArrayList<>();
        while(numberOfDrivers > 0) {
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
            numberOfDrivers --;
        }
        return listOfDriversToReturn;
    }

    private void calculateLeftTime() {
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

    private double calculateDistanceBetweenTwoCities(City cityFrom, City cityTo) {
        return getDist(cityFrom.getLat(), cityFrom.getLng(), cityTo.getLat(), cityTo.getLng());
    }

    private double getDist(double lat1, double lon1, double lat2, double lon2){
        int R = 6373; // radius of the earth in kilometres
        double lat1rad = Math.toRadians(lat1);
        double lat2rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(lat1rad) * Math.cos(lat2rad) *
                        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }

}
