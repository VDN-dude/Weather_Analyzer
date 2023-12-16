package com.example.spring_boot_calc.repository;

import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WeatherRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeAll
    static void beforeAll(){
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
    void should_Check_Existence_By_Status(){
        assertTrue(weatherRepository.existsByStatus(WeatherStatus.CURRENT));
        assertTrue(weatherRepository.existsByStatus(WeatherStatus.FORECAST));
        weatherRepository.deleteAll();
        assertFalse(weatherRepository.existsByStatus(WeatherStatus.CURRENT));
    }

    @Test
    void should_Find_Weather_With_Status_Current(){
        Optional<Weather> current = weatherRepository.findCurrent();
        assertTrue(current.isPresent());
        weatherRepository.deleteAll();
        current = weatherRepository.findCurrent();
        assertFalse(current.isPresent());
    }

    @Test
    void should_Calculate_Average_TempCelsius_Rounded_To_1_Decimal(){
        LocalDateTime firstLocalDate = LocalDateTime.now();
        Timestamp firstDate = Timestamp.valueOf(firstLocalDate);
        Timestamp secondDate = Timestamp.valueOf(firstLocalDate.plusDays(2));

        Double avgTempC = weatherRepository.getAvgTempC(firstDate, secondDate);
        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);

        assertEquals(avgTempC, BigDecimal.valueOf((0.0 + (-10.1) + (-3.1))/3.0).round(mathContext).doubleValue());

        secondDate = Timestamp.valueOf(firstLocalDate.plusDays(1));
        avgTempC = weatherRepository.getAvgTempC(firstDate, secondDate);
        assertEquals(avgTempC, BigDecimal.valueOf((0.0 + (-10.1))/2.0).round(mathContext).doubleValue());
    }
}
