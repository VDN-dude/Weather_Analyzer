package com.example.spring_boot_calc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_Handle_DateTimeParserException() throws Exception {
        mockMvc.perform(get("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"from\":\"13-12-2023\",\"to\":\"14-12-2023\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400 BAD_REQUEST"));
    }
}
