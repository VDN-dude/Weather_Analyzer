package com.example.spring_boot_calc.controller;

import com.example.spring_boot_calc.entity.AverageForecast;
import com.example.spring_boot_calc.entity.AverageForecastDto;
import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class WeatherController {

    @Autowired
    private WeatherServiceImpl weatherServiceImpl;

    @GetMapping("current")
    public ResponseEntity<?> getCurrentWeather(){
        Optional<Weather> current = weatherServiceImpl.getCurrent();

        if (current.isPresent()){
            return ResponseEntity.ok(current.get());
        } else {
            return new ResponseEntity<>("Something went wrong! Please try again later.", HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("forecast")
    public ResponseEntity<?> getAverageForecast(@Valid @RequestBody AverageForecastDto averageForecastDto,
                                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> map = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(map);
        }

        Optional<AverageForecast> avgTempC = weatherServiceImpl.getAvgTempC(averageForecastDto);

        if (avgTempC.isPresent()){
            return ResponseEntity.ok(avgTempC.get());
        } else {
            return new ResponseEntity<>("Something went wrong! Please try again later.", HttpStatus.NO_CONTENT);
        }
    }
}
