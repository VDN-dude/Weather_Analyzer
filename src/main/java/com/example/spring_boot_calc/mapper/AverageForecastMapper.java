package com.example.spring_boot_calc.mapper;

import com.example.spring_boot_calc.entity.AverageForecast;
import com.example.spring_boot_calc.entity.AverageForecastDto;
import com.example.spring_boot_calc.service.WeatherServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = WeatherServiceImpl.class,
        imports = Timestamp.class)
@Component
public interface AverageForecastMapper {

    @Mapping(target = "from", expression = "java(Timestamp.valueOf(averageForecastDto.getFrom().atStartOfDay()))")
    @Mapping(target = "to", expression = "java(Timestamp.valueOf(averageForecastDto.getTo().atTime(23,59)))")
    AverageForecast avgFcstDtoToAvgFcst(AverageForecastDto averageForecastDto);

}
