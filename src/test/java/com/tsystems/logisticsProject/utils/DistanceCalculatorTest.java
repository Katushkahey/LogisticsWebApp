package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.entity.City;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DistanceCalculatorTest {

    private DistanceCalculator calculator;

    @Mock
    private CityDao cityDao;

    @Before
    public void setup() {
        calculator = new DistanceCalculator();
    }

    @Test
    public void testGetDist() throws Exception {
        City SaintPetersburg = new City();
        SaintPetersburg.setLat(59.937500);
        SaintPetersburg.setLng(30.308611);
        City Moskow = new City();
        Moskow.setLat(55.751244);
        Moskow.setLng(37.618423);
        Double expectedDistance = 634.9622800559816;
        Double distTest = calculator.getDist(SaintPetersburg.getLat(), SaintPetersburg.getLng(), Moskow.getLat(), Moskow.getLng());
        Assert.assertEquals(expectedDistance, distTest);
    }
}
