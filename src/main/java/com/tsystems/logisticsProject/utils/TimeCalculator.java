package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TimeCalculator {

    public int fullDaysToEndMonth;
    public int hoursToEndMonth;
    private final int TIME_OF_ORDER_FOR_ONE_DRIVER_MAX = 12;
    private final int AVERAGE_VELOCITY = 60;
    private  final int NUMBER_OF_HOURS_IN_DAY = 24;

    private OrderService orderService;
    private DistanceCalculator distanceCalculator;

    @Autowired
    public void setDependencies(OrderService orderService, DistanceCalculator distanceCalculator) {
        this.orderService = orderService;
        this.distanceCalculator = distanceCalculator;
    }

    /**
     * calculate how much hourse can be spent by driver in this month to define
     * that he is suitable
     * @return max number of spent hours for searching driver
     */
    public int calculateMaxSpentTimeForDriver(int hoursToCompleteOrder, int numberOfWorkingHoursPerDayPerPerson,
                                              int numberOfDrivers) {
        int requiredNumberOfHoursPerPerson = calculateRequiredNumberOfHoursPerPerson(hoursToCompleteOrder,
                numberOfWorkingHoursPerDayPerPerson, numberOfDrivers);
        return Driver.MAX_HOURS_IN_MONTH - requiredNumberOfHoursPerPerson;
    }

    /**
     * calculate necessary number of valid working time for every driver depending on number of days to
     * complete month
     * @return necessary number of valid hours per driver for searching
     */
    public int calculateRequiredNumberOfHoursPerPerson(int hoursToCompleteOrder, int numberOfWorkingHoursPerDayPerPerson,
                                                       int numberOfDrivers) {
        if (hoursToCompleteOrder <= TIME_OF_ORDER_FOR_ONE_DRIVER_MAX) {
            if (hoursToCompleteOrder > hoursToEndMonth) {
                return hoursToEndMonth;
            } else {
                return hoursToCompleteOrder;
            }
        } else {
            int numberOfTotalWorkingHoursPerDay = numberOfDrivers * numberOfWorkingHoursPerDayPerPerson;
            int numberOfDaysToCompleteOrder = calculateRequiredNumberOfDaysToCompleteOrder(hoursToCompleteOrder, numberOfTotalWorkingHoursPerDay);
            if (numberOfDaysToCompleteOrder > fullDaysToEndMonth) {
                return fullDaysToEndMonth * numberOfWorkingHoursPerDayPerPerson;
            } else {
                return numberOfDaysToCompleteOrder * numberOfWorkingHoursPerDayPerPerson;
            }
        }
    }

    public int calculateRequiredNumberOfDaysToCompleteOrder(int hoursForOrderFromThisCity, int numberOfTotalWorkingHoursPerDay) {
        return (int)Math.ceil((double) hoursForOrderFromThisCity / (double)numberOfTotalWorkingHoursPerDay);
    }

    public int calculateTotalHoursForOrderFromThisCity(int optionalMaxDriversForOrderFromThisCity, int hoursForOrderFromThisCity,
                                                       int numberOfDrivers) {
        int numberOfWorkingHoursInDayPerPerson;
        if (optionalMaxDriversForOrderFromThisCity == 1) {
            return hoursForOrderFromThisCity;
        } else if (optionalMaxDriversForOrderFromThisCity == 2) {
            numberOfWorkingHoursInDayPerPerson = 12;
            return hoursForOrderFromThisCity * NUMBER_OF_HOURS_IN_DAY / (numberOfWorkingHoursInDayPerPerson * numberOfDrivers);
        } else {
            numberOfWorkingHoursInDayPerPerson = 8;
            return hoursForOrderFromThisCity * NUMBER_OF_HOURS_IN_DAY / (numberOfWorkingHoursInDayPerPerson * numberOfDrivers);
        }
    }

    /**
     * calculate hours for driving all distance including way from home and coming back depending on
     * received average velocity and hours for loading and unloading cargoes.
     * @return total required number of hours to complete order
     */
    public Map<City, Integer> calculateTimeForOrderFromEveryCity(Set<City> setOfCities, Long orderId) {
        Map<City, Integer> mapOfHoursForEveryStartCity = new HashMap<>();
        for (City city : setOfCities) {
            List<Waypoint> listOfWaypoints = orderService.findWaypointsForCurrentOrderById(orderId);
            double totalDistance = distanceCalculator.calculateTotalDistanceForOrderDependingOnStartCity(city, listOfWaypoints);
            int hoursForLoadingUnloading = calculateHoursForLoadingUnloading(listOfWaypoints);
            int hoursForDriving = calculateHoursForDrivingByDistance(totalDistance);
            int totalalNumberOfHours = hoursForDriving + hoursForLoadingUnloading;
            mapOfHoursForEveryStartCity.put(city, totalalNumberOfHours);
        }
        return mapOfHoursForEveryStartCity;
    }

    /**
     * calculate hours for driving all distance including way from home and coming back depending on
     * received average velocity
     * @return required number of hours for driving only to complete order
     */
    public int calculateHoursForDrivingByDistance(Double distance) {
        return (int) Math.ceil(distance / (double)AVERAGE_VELOCITY);
    }

    /**
     * calculate time for loading and unloading all of cargoes from order.
     * 1 hour is given for both loading and unloading every cargo.
     * @return total time for loading and unloading
     */
    public int calculateHoursForLoadingUnloading(List<Waypoint> listOfWaypoints) {
        return listOfWaypoints.size() / 2;
    }

    public int calculateLeftTimeToEndMonth() {
        int year = YearMonth.now().getYear();
        int month = YearMonth.now().getMonthValue();

        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        int today = LocalDate.now().getDayOfMonth();

        int hour = LocalDateTime.now().getHour();
        fullDaysToEndMonth = daysInMonth - today;
        int hoursInMonth = daysInMonth * NUMBER_OF_HOURS_IN_DAY;
        int hoursFromStartMonth = (today - 1) * NUMBER_OF_HOURS_IN_DAY + hour;
        hoursToEndMonth = hoursInMonth - hoursFromStartMonth;
        return fullDaysToEndMonth;
    }
}
