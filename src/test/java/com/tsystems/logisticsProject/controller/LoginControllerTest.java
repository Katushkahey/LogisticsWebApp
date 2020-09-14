package com.tsystems.logisticsProject.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginControllerTest {
    private MockMvc mockMvc;
    private LoginController controller;

    @Before
    public void setup() {
        controller = new LoginController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testSuccessfulLoginRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl(" /login"));
    }
}