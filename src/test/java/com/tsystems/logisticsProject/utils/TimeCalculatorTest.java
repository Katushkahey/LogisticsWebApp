package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.Waypoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TimeCalculatorTest {

    @Mock
    private TimeCalculator calculator;
    private List<Waypoint> listOfFourWaypoints;

    private Waypoint SpbPoint;
    private Waypoint MscPoint;

    @Before
    public void setup() {
        calculator = new TimeCalculator();
        prefillTestData();
    }

    @Test
    public void testCalculateLeftTimeToEndMonth() {
        int expectedNumberOfDays = 16;
        int fullDaysToEndMonth = calculator.calculateLeftTimeToEndMonth();
        Assert.assertEquals(expectedNumberOfDays, fullDaysToEndMonth);
    }

    @Test
    public void testHoursForLoadingUnloading() {
        int expectedNumberOfHours = 2;
        int testHours = calculator.calculateHoursForLoadingUnloading(listOfFourWaypoints);
        Assert.assertEquals(expectedNumberOfHours, testHours);
    }

    @Test
    public void testCalculateHoursForDrivingByDistance() {
        double distance = 605;
        int expectedNumberOfHours = 11;
        int testNumberOfHours = calculator.calculateHoursForDrivingByDistance(distance);
        Assert.assertEquals(expectedNumberOfHours, testNumberOfHours);
    }

    @Test
    public void testCalculateTotalHoursForOrderFromThisCity() {
        int optionalNumberOfDrivers = 3;
        int hoursForOrderFromThisCity = 96;
        int numberOfDrivers = 2;
        int expectedNumberOfHours = 96 / 8 / 2 * 24;
        int testNumberOfHours = calculator.calculateTotalHoursForOrderFromThisCity(optionalNumberOfDrivers,
                hoursForOrderFromThisCity, numberOfDrivers);
        Assert.assertEquals(expectedNumberOfHours, testNumberOfHours);
    }

    @Test
    public void testCalculateRequiredNumberOfDaysToCompleteOrder() {
        int totalHoursToCompleteOrder = 98;
        int numberOfTotalWorkingHoursPerDay = 16;
        int expectedNumberOfDays = 7;
        int testNumberOfDays = calculator.calculateRequiredNumberOfDaysToCompleteOrder(totalHoursToCompleteOrder,
                numberOfTotalWorkingHoursPerDay);
        Assert.assertEquals(expectedNumberOfDays, testNumberOfDays);
    }

    @Test
    public void testCalculateRequiredNumberOfHoursPerPerson() {
        int hoursToCompleteOrder = 96;
        int numberOfWorkingHoursPerDayPerPerson = 8;
        int numberOfDrivers = 2;
        int expectedNumberOfHours = 48;
        int testNumberOfHours = calculator.calculateRequiredNumberOfHoursPerPerson(hoursToCompleteOrder,
                numberOfWorkingHoursPerDayPerPerson, numberOfDrivers);
        Assert.assertEquals(expectedNumberOfHours, testNumberOfHours);
    }

    @Test
    public void testCalculateMaxSpentTimeForDriver() {
        int hoursToCompleteOrder = 96;
        int numberOfWorkingHoursPerDayPerPerson = 8;
        int numberOfDrivers = 2;
        int expectedNumberOfHours = 176 - 48;
        int testNumberOfHours = calculator.calculateMaxSpentTimeForDriver(hoursToCompleteOrder, numberOfWorkingHoursPerDayPerPerson,
                numberOfDrivers);
        Assert.assertEquals(expectedNumberOfHours, testNumberOfHours);
    }

    public void prefillTestData () {
        listOfFourWaypoints = new ArrayList<>();
        SpbPoint = new Waypoint();
        MscPoint = new Waypoint();

        listOfFourWaypoints.add(SpbPoint);
        listOfFourWaypoints.add(MscPoint);
        listOfFourWaypoints.add(SpbPoint);
        listOfFourWaypoints.add(MscPoint);
        calculator.calculateLeftTimeToEndMonth();
    }
}
