package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.City;
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
        int expextedNumberOfHours = 11;
        int testNumberOfHours = calculator.calculateHoursForDrivingByDistance(distance);
        Assert.assertEquals(expextedNumberOfHours, testNumberOfHours);
    }

    private void prefillTestData () {
        listOfFourWaypoints = new ArrayList<>();
        SpbPoint = new Waypoint();
        MscPoint = new Waypoint();

        listOfFourWaypoints.add(SpbPoint);
        listOfFourWaypoints.add(MscPoint);
        listOfFourWaypoints.add(SpbPoint);
        listOfFourWaypoints.add(MscPoint);
    }
}
