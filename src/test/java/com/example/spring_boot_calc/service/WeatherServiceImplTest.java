package com.example.spring_boot_calc.service;

import com.example.spring_boot_calc.entity.AverageForecast;
import com.example.spring_boot_calc.entity.AverageForecastDto;
import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import com.example.spring_boot_calc.repository.WeatherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import({ObjectMapper.class})
public class WeatherServiceImplTest {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherRepository weatherRepository;
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");


    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

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
    void should_Give_Optional_Of_Weather_With_Status_Current(){
        Optional<Weather> current = weatherService.getCurrent();
        assertTrue(current.isPresent());
        weatherRepository.deleteAll();
        current = weatherRepository.findCurrent();
        assertTrue(current.isEmpty());
    }

    @Test
    void should_Calculate_Average_TempCelsius_Rounded_To_1_Decimal(){
        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
        LocalDate firstDate = LocalDate.now();
        LocalDate secondDate = firstDate.plusDays(2);
        AverageForecastDto averageForecastDto = new AverageForecastDto(firstDate, secondDate);

        Optional<AverageForecast> optionalAverageForecast = weatherService.getAvgTempC(averageForecastDto);

        assertTrue(optionalAverageForecast.isPresent());
        AverageForecast averageForecast = optionalAverageForecast.get();
        assertEquals(averageForecast.getAvgTempC(), BigDecimal.valueOf((0.0 + (-10.1) + (-3.1))/3.0).round(mathContext).doubleValue());

        weatherRepository.deleteAll();
        optionalAverageForecast = weatherService.getAvgTempC(averageForecastDto);
        assertFalse(optionalAverageForecast.isPresent());
    }
}
