package com.example.spring_boot_calc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class WeatherAnalyzerApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "Europe/Moscow");
        SpringApplication.run(WeatherAnalyzerApplication.class, args);
    }
}
