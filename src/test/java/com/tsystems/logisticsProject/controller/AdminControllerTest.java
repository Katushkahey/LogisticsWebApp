package com.tsystems.logisticsProject.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
    private MockMvc mockMvc;
    private AdminController controller;

    @Before
    public void setup() {
        controller = new AdminController ();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @After
    public void cleanup() {
        Mockito.reset();
    }

    @Test
    public void testOkRedirectWithoutLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
//                .andExpectredirectedUrl(" /login/"));
    }
}
