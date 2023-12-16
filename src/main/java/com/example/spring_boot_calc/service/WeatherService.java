package com.example.spring_boot_calc.service;

import com.example.spring_boot_calc.entity.AverageForecast;
import com.example.spring_boot_calc.entity.AverageForecastDto;
import com.example.spring_boot_calc.entity.Weather;

import java.util.Optional;

public interface WeatherService {

    Optional<Weather> getCurrent();

    Optional<AverageForecast> getAvgTempC(AverageForecastDto averageForecastDto);
}
