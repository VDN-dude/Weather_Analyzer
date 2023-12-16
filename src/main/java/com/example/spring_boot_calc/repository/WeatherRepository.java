package com.example.spring_boot_calc.repository;

import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query(value = "select * from Weather WHERE status = 'CURRENT'", nativeQuery = true)
    Optional<Weather> findCurrent();

    @Query(value = "select round( cast( avg(temp_c) as numeric), 1) from Weather where status = 'FORECAST' and date >= ?1 and date <= ?2", nativeQuery = true)
    Double getAvgTempC(Timestamp from, Timestamp to);

    boolean existsByStatus(WeatherStatus status);
}
