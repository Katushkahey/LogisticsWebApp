package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.dao.CityDao;
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
public class DistanceCalculatorTest {

    @Mock
    private CityDao cityDao;
    private DistanceCalculator calculator;

    private City Moscow;
    private City SaintPetersburg;
    private City Novosibirsk;

    private Waypoint SpbPoint;
    private Waypoint MscPoint;

    private List<Waypoint> listOfThreeWaypoints;

    @Before
    public void setup() {
        calculator = new DistanceCalculator();
        prefillTestData();
    }

    @Test
    public void testGetDist() {
        Double expectedDistance = 634.9622800559816;
        Double distTest = calculator.getDist(SaintPetersburg.getLat(), SaintPetersburg.getLng(), Moscow.getLat(), Moscow.getLng());
        Assert.assertEquals(expectedDistance, distTest);
    }

    @Test
    public void testCalculateDistanceBetweenTwoCities() {
        Double expectedDistance = 634.9622800559816;
        Double distTest = calculator.calculateDistanceBetweenTwoCities(Moscow, SaintPetersburg);
        Assert.assertEquals(expectedDistance, distTest);
    }

    @Test
    public void testCalculateDistanceBetweenWaypointsForOrder() {
        Double expectedDistance = 634.9622800559816 * 2;
        Double distTest = calculator.calculateDistanceBetweenWaypointsForOrder(listOfThreeWaypoints);
        Assert.assertEquals(expectedDistance, distTest);
    }

    @Test
    public void testCalculateTotalDistanceForOrderDependingOnStartCity() {
        Double expectedDistance = 634.9622800559816 * 4;
        Double distTest = calculator.calculateTotalDistanceForOrderDependingOnStartCity(Moscow, listOfThreeWaypoints);
        Assert.assertEquals(expectedDistance, distTest);
    }

    private void prefillTestData() {
        SaintPetersburg = new City();
        SaintPetersburg.setLat(59.937500);
        SaintPetersburg.setLng(30.308611);

        Moscow = new City();
        Moscow.setLat(55.751244);
        Moscow.setLng(37.618423);

        Novosibirsk = new City();
        Novosibirsk.setLat(55.018803);
        Novosibirsk.setLng(47.233334);


        SpbPoint = new Waypoint();
        SpbPoint.setCity(SaintPetersburg);

        MscPoint = new Waypoint();
        MscPoint.setCity(Moscow);

        listOfThreeWaypoints = new ArrayList<>();
        listOfThreeWaypoints.add(SpbPoint);
        listOfThreeWaypoints.add(MscPoint);
        listOfThreeWaypoints.add(SpbPoint);
    }
}
