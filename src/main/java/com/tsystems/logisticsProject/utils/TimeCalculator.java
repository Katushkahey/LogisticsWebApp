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

    /** считаем максимальное количество часов ,которое может уже быть потрачено водителем на момент назначения заказа, что бы
     * ему хватило времени на выполнение заказа
     */

    public int calculateMaxSpentTimeForDriver(int hoursToCompleteOrder, int numberOfWorkingHoursPerDayPerPerson,
                                              int numberOfDrivers) {
        int requiredNumberOfHoursPerPerson = calculateRequiredNumberOfHoursPerPerson(hoursToCompleteOrder,
                numberOfWorkingHoursPerDayPerPerson, numberOfDrivers);
        return Driver.MAX_HOURS_IN_MONTH - requiredNumberOfHoursPerPerson;
    }

    /** если необходимое кол-во дней на выполнение заказа больше ,чем кол-во дней до конца месяца,
     * то для поиска водителей время будет равно кол-ву дней до конца месяца * на 8 часов каждый день,
     * тк после этого их часы обнулятся.
     * Иначе, время для поиска водителя = полное время, необходимое на выполнение заказа/2, тк выполнят его пополам.
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

    public int calculateHoursForDrivingByDistance(Double distance) {
        return (int) Math.ceil(distance / (double)AVERAGE_VELOCITY);
    }

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
