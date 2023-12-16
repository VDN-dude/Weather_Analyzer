package com.example.spring_boot_calc.controller;

import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import com.example.spring_boot_calc.repository.WeatherRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();

        LocalDateTime now = LocalDateTime.now();
        Timestamp updatedAt = Timestamp.valueOf(now.minusMinutes(10));

        weatherRepository.save(new Weather(1L,
                -0.2,
                4.1,
                1020.0,
                86.0,
                "Light snow",
                Timestamp.valueOf(now),
                updatedAt,
                WeatherStatus.CURRENT));
        weatherRepository.save(new Weather(2L,
                0.0,
                3.2,
                1000.0,
                80.0,
                "Foggy",
                Timestamp.valueOf(now.plusHours(3)),
                updatedAt,
                WeatherStatus.FORECAST));
        weatherRepository.save(new Weather(3L,
                -10.1,
                5.0,
                1060.0,
                87.0,
                "Blizzard",
                Timestamp.valueOf(now.plusDays(1)),
                updatedAt,
                WeatherStatus.FORECAST));
        weatherRepository.save(new Weather(4L,
                -3.1,
                3.0,
                1020.0,
                82.0,
                "Foggy",
                Timestamp.valueOf(now.plusDays(2)),
                updatedAt,
                WeatherStatus.FORECAST));
    }

    @Test
    public void should_Return_Current_Weather() throws Exception {
        mockMvc.perform(get("/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tempC").isNumber())
                .andExpect(jsonPath("$.tempC").value(-0.2))
                .andExpect(jsonPath("$.windMph").isNumber())
                .andExpect(jsonPath("$.windMph").value(4.1))
                .andExpect(jsonPath("$.pressureMb").isNumber())
                .andExpect(jsonPath("$.pressureMb").value(1020.0))
                .andExpect(jsonPath("$.humidity").isNumber())
                .andExpect(jsonPath("$.humidity").value(86.0))
                .andExpect(jsonPath("$.condition").isString())
                .andExpect(jsonPath("$.condition").value("Light snow"));
    }

    @Test
    public void should_Return_Average_Forecast_Weather() throws Exception {
        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
        LocalDate now = LocalDate.now();
        mockMvc.perform(get("/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"from\":\"" + now + "\",\"to\":\"" + now.plusDays(1) + "\"}"))
                .andExpect(jsonPath("$.avgTempC").value(BigDecimal.valueOf((0.0 + (-10.1))/2).round(mathContext)));
    }

    @Test
    public void should_Return_Http_Status_No_Content() throws Exception {
        weatherRepository.deleteAll();

        mockMvc.perform(get("/current"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/forecast")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"from\":\"2023-12-16\",\"to\":\"2023-12-17\"}"))
                .andExpect(status().isNoContent());
    }
}
