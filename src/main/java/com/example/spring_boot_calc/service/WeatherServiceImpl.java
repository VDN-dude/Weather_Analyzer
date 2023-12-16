package com.example.spring_boot_calc.service;

import com.example.spring_boot_calc.entity.AverageForecast;
import com.example.spring_boot_calc.entity.AverageForecastDto;
import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import com.example.spring_boot_calc.mapper.AverageForecastMapper;
import com.example.spring_boot_calc.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService{
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private AverageForecastMapper averageForecastMapper;
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Optional<Weather> getCurrent(){
        boolean existence = weatherRepository.existsByStatus(WeatherStatus.CURRENT);
        Optional<Weather> current;

        if (existence){
            current = weatherRepository.findCurrent();
        } else {
            current = Optional.empty();
            logger.error("Current weather is not exist in the database! time : " + LocalDateTime.now());
        }
        return current;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AverageForecast> getAvgTempC(AverageForecastDto averageForecastDto){
        boolean existence = weatherRepository.existsByStatus(WeatherStatus.FORECAST);
        Optional<AverageForecast> optAverageForecast;

        if (existence){
            AverageForecast averageForecast = averageForecastMapper.avgFcstDtoToAvgFcst(averageForecastDto);
            Double avgTempC = weatherRepository.getAvgTempC(averageForecast.getFrom(), averageForecast.getTo());
            averageForecast.setAvgTempC(avgTempC);
            optAverageForecast = Optional.of(averageForecast);
        } else {
            optAverageForecast = Optional.empty();
            logger.error("Forecast is not exist in the database! time : " + LocalDateTime.now());
        }
        return optAverageForecast;
    }
}
