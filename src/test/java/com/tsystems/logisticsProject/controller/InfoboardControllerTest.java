package com.tsystems.logisticsProject.controller;

import com.tsystems.logisticsProject.service.DriverService;
import com.tsystems.logisticsProject.service.OrderService;
import com.tsystems.logisticsProject.service.TruckService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class InfoboardControllerTest {
    private MockMvc mockMvc;
    private InfoboardController controller;

    @Mock private OrderService orderService;
    @Mock private DriverService driverService;
    @Mock private TruckService truckService;

    @Before
    public void setup() {
        controller = new InfoboardController(orderService, driverService, truckService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}