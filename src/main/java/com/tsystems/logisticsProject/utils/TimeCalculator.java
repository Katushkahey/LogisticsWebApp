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
    private final int AVERAGE_VELOCITY = 60;
    private  final int NUMBER_OF_HOURS_IN_DAY = 24;

    private OrderService orderService;
    private DistanceCalculator distanceCalculator;

    @Autowired
    public void setDependencies(OrderService orderService, DistanceCalculator distanceCalculator) {
        this.orderService = orderService;
        this.distanceCalculator = distanceCalculator;
    }

    public int calculateMaxSpentTimeForDriver(int optionalMaxDrivers, int hoursForOrderFromThisCity, int numberOfDrivers) {
        int requiredNumberOfHoursPerPerson = calculateRequiredNumberOfHoursPerPerson(optionalMaxDrivers,
                hoursForOrderFromThisCity, numberOfDrivers);
        return Driver.MAX_HOURS_IN_MONTH - requiredNumberOfHoursPerPerson;
    }

    private int calculateRequiredNumberOfHoursPerPerson(int optionalMaxDrivers, int hoursForOrderFromThisCity,
                                                        int numberOfDrivers) {
        if (optionalMaxDrivers == 1) {
            return Math.min(hoursToEndMonth, hoursForOrderFromThisCity);
        } else {
            int numberOfWorkingHoursInDayPerPerson = returnNormalNumberOfWorkingHoursInDayPerPerson(optionalMaxDrivers);
            int numberOfTotalWorkingTimePerDay = numberOfWorkingHoursInDayPerPerson * numberOfDrivers;
            int numberOfDayToCompleteOrder = (int) Math.ceil(hoursForOrderFromThisCity / numberOfWorkingHoursInDayPerPerson);
            if (numberOfDayToCompleteOrder > fullDaysToEndMonth) {
                return fullDaysToEndMonth * numberOfTotalWorkingTimePerDay;
            } else {
                return (int) Math.ceil(hoursForOrderFromThisCity / numberOfDrivers);
            }
        }
    }

    private int returnNormalNumberOfWorkingHoursInDayPerPerson(int optionalMaxDrivers) {
        if (optionalMaxDrivers == 3) {
            return 8;
        } else {
            return 12;
        }
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

    public Map<City, Integer> calculateTimeForOrderFromEveryCity(Set<City> setOfCities, Long orderId) {
        Map<City, Integer> mapOfHoursForEveryStartCity = new HashMap<>();
        for (City city : setOfCities) {
            List<Waypoint> listOfWaypoints = orderService.findWaypointsForCurrentOrderById(orderId);
            double totalDistance = distanceCalculator.calculateTotalDistanceForOrderDependingOnStartCity(city, listOfWaypoints);
            int hoursForLoadingUnloading = calculateHoursForLoadingUnloading(orderId);
            int hoursForDriving = calculateHoursForDrivingByDistance(totalDistance);
            int totalalNumberOfHours = hoursForDriving + hoursForLoadingUnloading;
            mapOfHoursForEveryStartCity.put(city, totalalNumberOfHours);
        }
        return mapOfHoursForEveryStartCity;
    }

    private int calculateHoursForDrivingByDistance(Double distance) {
        return (int) Math.ceil(distance / AVERAGE_VELOCITY);
    }

    private int calculateHoursForLoadingUnloading(Long orderId) {
        return orderService.findWaypointsForCurrentOrderById(orderId).size() / 2;
    }

    public void calculateLeftTimeToEndMonth() {
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
}
