package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.mapper.CombinationForOrderMapper;
import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.utils.AssignmentParametersCalculator;
import com.tsystems.logisticsProject.utils.TimeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class OrderAssignmentService {


    private List<CombinationForOrderDto> listOfCombinationForOrderDto;
    private long combinationId;
    private DriverService driverService;
    private CombinationForOrderMapper combinationForOrderMapper;
    private TimeCalculator timeCalculator;
    private AssignmentParametersCalculator assignmentParametersCalculator;

    private static final Logger LOG = Logger.getLogger(OrderAssignmentService.class.getName());

    @Autowired
    private void setDependencies(DriverService driverService, CombinationForOrderMapper
                                 combinationForOrderMapper, TimeCalculator timeCalculator,
                                 AssignmentParametersCalculator assignmentParametersCalculator) {
        this.driverService = driverService;
        this.assignmentParametersCalculator = assignmentParametersCalculator;
        this.combinationForOrderMapper = combinationForOrderMapper;
        this.timeCalculator = timeCalculator;
    }

    /**
     * select combinations of drivers and truck for order depending on capacity, required number of hours
     * to complete order for every driver.
     * @return list of combinations with commercial performance
     */
    public List<CombinationForOrderDto> createListOfCombinationsForOrder(Long orderId) {
        listOfCombinationForOrderDto = new ArrayList<>();
        combinationId = 0;
        timeCalculator.calculateLeftTimeToEndMonth();
        Map<City, List<Truck>> mapOfTrucksForEveryCity = assignmentParametersCalculator.getMapOfTrucksForEveryCity(orderId);
        Set<City> setOfCity = mapOfTrucksForEveryCity.keySet();
        Map<City, Integer> mapOfMaxOptionalNumberOfDrivers = assignmentParametersCalculator.calculateMaxOptionalNumberOfDriversForOrderFromEveryCity(setOfCity, orderId);
        Map<City, Integer> mapOfHoursForOrderFromEveryCity = timeCalculator.calculateTimeForOrderFromEveryCity(setOfCity, orderId);

        for (City city : setOfCity) {

            List<Truck> listOfTruckForCurrentOrderInEveryCity = mapOfTrucksForEveryCity.get(city);
            int optionalMaxDriversForOrderFromThisCity = mapOfMaxOptionalNumberOfDrivers.get(city);
            int hoursForOrderFromThisCity = mapOfHoursForOrderFromEveryCity.get(city);

            for (Truck truck : listOfTruckForCurrentOrderInEveryCity) {

                /*
                 * truck`s crew size can be only 2 or 3. Required number of drivers for order can be onle 1, 2 and 3,
                 * so if truck`s crew size < than required number of drivers that means that crew size is 2 and
                 * required number of drivers is three.
                 * That`s mean that time to complete order is more than 48 hours and working hours for every driver per
                 *  day is 8.
                 */
                if (truck.getCrewSize() < optionalMaxDriversForOrderFromThisCity) {
                    int numberOfDrivers = 2;
                    int workingHoursPerDayForEveryPerson = 8;

                    int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                            workingHoursPerDayForEveryPerson, numberOfDrivers);

                    List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                    if (listOfDrivers != null && listOfDrivers.size() != 0) {

                        /* We know, that crew size is 2, so if number of suitable drivers is less, that means that we
                         * have only one suitable driver. That means that he will carry out the order longer,
                         * so we should recalculate required number of hours to complete the order
                         *  and check if this driver still suitable.
                         */
                        if (listOfDrivers.size() < truck.getCrewSize()) {
                            numberOfDrivers = 1;
                            maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                    workingHoursPerDayForEveryPerson, numberOfDrivers);

                            if (listOfDrivers.get(0).getHoursThisMonth() <= maxSpentTimeForDriver) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers,
                                        optionalMaxDriversForOrderFromThisCity);
                            }

                        } else if (listOfDrivers.size() == truck.getCrewSize()) {
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);

                            /* if number of drivers more than we need we should choose those of them, who have min valid
                             *  time in this month in order to save people with more valid time for longer orders.
                             */
                        } else {
                            List<Driver> listOfDriversForCombination =
                                    assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 2);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
                        }
                    }
                } else {

                    /* if required number of drivers to complete order is 1, that means that total hours to complete
                     * order not more than 12 hours and this is how long 1 driver will work per day.
                     */
                    if (optionalMaxDriversForOrderFromThisCity == 1) {
                        int numberOfDrivers = 1;
                        int workingHoursPerDayForEveryPerson = hoursForOrderFromThisCity;
                        int maxSpentTimeForDriver =
                                timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity, workingHoursPerDayForEveryPerson,
                                        numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);

                        if (listOfDrivers != null && listOfDrivers.size() != 0) {
                            List<Driver> listOfDriversToCombination =
                                    assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 1);
                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversToCombination, optionalMaxDriversForOrderFromThisCity);
                        }

                        /* if required number of drivers to complete order is 2 that means that total hours to
                         *   complete order between 12 and 48 hours. Number of working hours per person per day is 12,
                         *   because order will be complete for 2 days in this case.
                         */
                    } else if (optionalMaxDriversForOrderFromThisCity == 2) {
                        int numberOfDrivers = 2;
                        int workingHoursPerDayForEveryPerson = 12;
                        int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {

                            /* if we found only 1 suitable driver that means that he will he will carry out the order
                             * longer, so we should recalculate required number of hours and check if this driver
                             *  still suitable
                             */
                            if (listOfDrivers.size() < 2) {
                                numberOfDrivers = 1;

                                int newMaxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                        workingHoursPerDayForEveryPerson, numberOfDrivers);

                                if (listOfDrivers.get(0).getHoursThisMonth() <= newMaxSpentTimeForDriver) {
                                    addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);
                                }

                            } else if (listOfDrivers.size() == 2) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers, optionalMaxDriversForOrderFromThisCity);

                            } else {
                                List<Driver> listOfDriversForCombination =
                                        assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 2);

                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination, optionalMaxDriversForOrderFromThisCity);
                            }
                        }
                        /* in this case we need 3 drivers and crew size of the truck is also 3.
                         * every driver shoul work only 8 hours per day because of long duration of order.
                         */
                    } else {
                        int numberOfDrivers = 3;
                        int workingHoursPerDayForEveryPerson = 8;
                        int maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                        List<Driver> listOfDrivers = driverService.findDriversForTruck(city, maxSpentTimeForDriver);
                        if (listOfDrivers != null && listOfDrivers.size() != 0) {

                            if (listOfDrivers.size() > numberOfDrivers) {
                                List<Driver> listOfDriversForCombination =
                                        assignmentParametersCalculator.returnDriversWithMinValidTimeAsList(listOfDrivers,
                                                numberOfDrivers);
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDriversForCombination,
                                        optionalMaxDriversForOrderFromThisCity);

                            } else if (listOfDrivers.size() == numberOfDrivers) {
                                addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers,
                                        optionalMaxDriversForOrderFromThisCity);

                            } else {
                                if (listOfDrivers.size() == 1) {
                                    numberOfDrivers = 1;
                                    maxSpentTimeForDriver =
                                            timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                            workingHoursPerDayForEveryPerson, numberOfDrivers);

                                    if (listOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity, listOfDrivers,
                                                optionalMaxDriversForOrderFromThisCity);
                                    }
                                } else {
                                    numberOfDrivers = 2;
                                    maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                            workingHoursPerDayForEveryPerson, numberOfDrivers);

                                    /* we reculcilated required number of hours per person, so we will check if
                                     * every driver in founded list is still suitable
                                     */
                                    List<Driver> newListOfDrivers = new ArrayList<>();
                                    for (Driver driver : listOfDrivers) {
                                        if (driver.getHoursThisMonth() < maxSpentTimeForDriver) {
                                            newListOfDrivers.add(driver);
                                        }
                                    }

                                    if (newListOfDrivers.size() == 2) {
                                        addCombinationForOrderInList(truck, hoursForOrderFromThisCity,
                                                newListOfDrivers, optionalMaxDriversForOrderFromThisCity);

                                    } else if (newListOfDrivers.size() == 1) {
                                        numberOfDrivers = 1;
                                        maxSpentTimeForDriver = timeCalculator.calculateMaxSpentTimeForDriver(hoursForOrderFromThisCity,
                                                workingHoursPerDayForEveryPerson, numberOfDrivers);
                                        if (newListOfDrivers.get(0).getHoursThisMonth() < maxSpentTimeForDriver) {
                                            addCombinationForOrderInList(truck, hoursForOrderFromThisCity, newListOfDrivers,
                                                    optionalMaxDriversForOrderFromThisCity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return listOfCombinationForOrderDto;
    }

    /**
     * create new combination of drivers and truck, set all parameters
     * and put it to total list of combinations
     */
    private void addCombinationForOrderInList(Truck truck, int hoursForOrderFromThisCity, List<Driver> listOfDriversForCombination, int optionalMaxDriversForOrderFromThisCity) {
        combinationId++;
        CombinationForOrder combinationForOrder = new CombinationForOrder();
        combinationForOrder.setId(combinationId);
        combinationForOrder.setListOfDrivers(listOfDriversForCombination);
        combinationForOrder.setTruck(truck);
        int numberOfDrivers = listOfDriversForCombination.size();
        int totalHours = timeCalculator.calculateTotalHoursForOrderFromThisCity(optionalMaxDriversForOrderFromThisCity,
                hoursForOrderFromThisCity, numberOfDrivers);
        combinationForOrder.setTotalHours(totalHours);
        combinationForOrder.setTotalBillableHours(hoursForOrderFromThisCity);
        listOfCombinationForOrderDto.add(combinationForOrderMapper.toDto(combinationForOrder));
        LOG.info("new combination to assign for order has been created");
    }

    public CombinationForOrderDto getCombinationForOrderByIndex(int index, String orderNumber) {
        CombinationForOrderDto combination = listOfCombinationForOrderDto.get(index);
        LOG.info("Combination of " + combination.getDrivers() + "and " + combination.getTruckNumber() + "has " +
                "been assignet for order " + orderNumber);
        return combination;
    }
}