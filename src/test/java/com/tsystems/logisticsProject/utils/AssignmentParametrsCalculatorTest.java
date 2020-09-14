package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
import com.tsystems.logisticsProject.service.impl.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentParametrsCalculatorTest {

    @Mock
    private AssignmentParametersCalculator calculator;

    private List<Truck>  listOfTrucks;
    private Set<City> setOfCities;
    private List<Driver> listOfDrivers;
    private List<Driver> listOfThreeDriversWithMaxSpentTime;
    private List<Driver> listOfOneDriverWithMaxSpentTime;

    @Before
    public void setup() {
        this.calculator = new AssignmentParametersCalculator();
        prefillTestData();
    }

    @Test
    public void testGetSetOfCitiesFromListOfTrucks() {
        Set<City> expectedSetOfCities = setOfCities;
        Set<City> testSetOfCities = calculator.getSetOfCitiesFromListOfTrucks(listOfTrucks);
        Assert.assertEquals(expectedSetOfCities, testSetOfCities);
    }

    @Test
    public void testReturnDriversWithMinValidTimeAsListForThree() {
        List<Driver> expectedListOfDriver = listOfThreeDriversWithMaxSpentTime;
        List<Driver> testListOfDriver = calculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 3);
        Assert.assertEquals(expectedListOfDriver, testListOfDriver);
    }

    @Test
    public void testReturnDriversWithMinValidTimeAsListForOne() {
        List<Driver> expectedListOfDriver = listOfOneDriverWithMaxSpentTime;
        List<Driver> testListOfDriver = calculator.returnDriversWithMinValidTimeAsList(listOfDrivers, 1);
        Assert.assertEquals(expectedListOfDriver, testListOfDriver);
    }

    private void prefillTestData() {
        City Spb = new City();
        City Msc = new City();
        City Novosibirsk = new City();

        Truck truck1 = new Truck();
        truck1.setCurrentCity(Spb);

        Truck truck2 = new Truck();
        truck2.setCurrentCity(Msc);

        Truck truck3 = new Truck();
        truck3.setCurrentCity(Novosibirsk);

        Truck truck4 = new Truck();
        truck4.setCurrentCity(Spb);

        listOfTrucks = new ArrayList<>();
        listOfTrucks.add(truck1);
        listOfTrucks.add(truck2);
        listOfTrucks.add(truck3);
        listOfTrucks.add(truck4);

        setOfCities = new HashSet<>();
        setOfCities.add(Spb);
        setOfCities.add(Msc);
        setOfCities.add(Novosibirsk);

        Driver driver1 = new Driver();
        driver1.setHoursThisMonth(100);

        Driver driver2 = new Driver();
        driver2.setHoursThisMonth(10);

        Driver driver3 = new Driver();
        driver3.setHoursThisMonth(90);

        Driver driver4 = new Driver();
        driver4.setHoursThisMonth(95);

        Driver driver5 = new Driver();
        driver5.setHoursThisMonth(110);

        Driver driver6 = new Driver();
        driver6.setHoursThisMonth(80);

        listOfDrivers = new ArrayList<>();
        listOfDrivers.add(driver1);
        listOfDrivers.add(driver2);
        listOfDrivers.add(driver3);
        listOfDrivers.add(driver4);
        listOfDrivers.add(driver5);
        listOfDrivers.add(driver6);

        listOfThreeDriversWithMaxSpentTime = new ArrayList<>();
        listOfThreeDriversWithMaxSpentTime.add(driver5);
        listOfThreeDriversWithMaxSpentTime.add(driver1);
        listOfThreeDriversWithMaxSpentTime.add(driver4);

        listOfOneDriverWithMaxSpentTime = new ArrayList<>();
        listOfOneDriverWithMaxSpentTime.add(driver5);


    }

}
