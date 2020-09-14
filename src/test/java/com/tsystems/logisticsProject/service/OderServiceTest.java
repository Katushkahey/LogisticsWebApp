package com.tsystems.logisticsProject.service;

import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.service.impl.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OderServiceTest {

    @Mock
    private OrderService orderService;

    private Cargo firstCargo;
    private Cargo secondCargo;

    private Waypoint firstLoadingWaypoint;
    private Waypoint secondLoadingWaypoint;
    private Waypoint firstUnloadingWaypoint;
    private Waypoint secondUnloadingWaypoint;

    private List<Waypoint> heavyList;
    private List<Waypoint> lightList;

    @Before
    public void setup() {
        this.orderService = new OrderServiceImpl();
        prefillTestData();
    }

    @Test
    public void testCalculateMaxOneTimeWeightForHeaviListr() {
        double expectedTotalWeight = 24000.0;
        double testTotalWeight = orderService.getMaxWeightForOrderById(heavyList);
        Assert.assertEquals(expectedTotalWeight, testTotalWeight, 1);
    }

    @Test
    public void testCalculateMaxOneTimeWeightForLightList() {

        double expectedTotalWeight = 14000.0;
        double testTotalWeight = orderService.getMaxWeightForOrderById(lightList);
        Assert.assertEquals(expectedTotalWeight, testTotalWeight, 1);
    }

    private void prefillTestData() {
        firstCargo = new Cargo();
        firstCargo.setWeight(14000.0);

        secondCargo = new Cargo();
        secondCargo.setWeight(10000.0);

        firstLoadingWaypoint = new Waypoint();
        firstLoadingWaypoint.setCargo(firstCargo);
        firstLoadingWaypoint.setAction(Action.LOADING);

        secondLoadingWaypoint = new Waypoint();
        secondLoadingWaypoint.setCargo(secondCargo);
        secondLoadingWaypoint.setAction(Action.LOADING);

        firstUnloadingWaypoint = new Waypoint();
        firstUnloadingWaypoint.setCargo(firstCargo);
        firstUnloadingWaypoint.setAction(Action.UNLOADING);

        secondUnloadingWaypoint = new Waypoint();
        secondUnloadingWaypoint.setCargo(secondCargo);
        secondUnloadingWaypoint.setAction(Action.UNLOADING);

        heavyList = new ArrayList<>();
        heavyList.add(firstLoadingWaypoint);
        heavyList.add(secondLoadingWaypoint);
        heavyList.add(firstUnloadingWaypoint);
        heavyList.add(secondUnloadingWaypoint);

        lightList = new ArrayList<>();
        lightList.add(firstLoadingWaypoint);
        lightList.add(firstUnloadingWaypoint);
        lightList.add(secondLoadingWaypoint);
        lightList.add(secondUnloadingWaypoint);
    }
}
