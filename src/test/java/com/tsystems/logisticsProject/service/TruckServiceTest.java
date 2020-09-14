package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.service.impl.TruckServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TruckServiceTest {

    @Mock
    private TruckServiceImpl truckService;
    private List<Truck> listOfTrucks;

    @Before
    public void setup() {
        this.truckService = new TruckServiceImpl();
        prefillTestData();
    }

    @Test
    public void testGetMaxCapacity() {
        double expectedMaxCapacity = 30.0 * 1000;
        double testMaxCapacity = truckService.calculateMaxCapacity(listOfTrucks);
        Assert.assertEquals(expectedMaxCapacity, testMaxCapacity, 1);
    }

    private void prefillTestData() {
        Truck truck1 = new Truck();
        truck1.setCapacity(25.0);

        Truck truck2 = new Truck();
        truck2.setCapacity(28.0);

        Truck truck3 = new Truck();
        truck3.setCapacity(30.0);

        Truck truck4 = new Truck();
        truck4.setCapacity(17.0);

        listOfTrucks = new ArrayList<>();
        listOfTrucks.add(truck1);
        listOfTrucks.add(truck2);
        listOfTrucks.add(truck3);
        listOfTrucks.add(truck4);
    }
}
