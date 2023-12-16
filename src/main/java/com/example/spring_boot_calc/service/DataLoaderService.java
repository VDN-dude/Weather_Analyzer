package com.example.spring_boot_calc.service;

import com.example.spring_boot_calc.config.ConfigProperties;
import com.example.spring_boot_calc.entity.Weather;
import com.example.spring_boot_calc.entity.WeatherStatus;
import com.example.spring_boot_calc.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public final class DataLoaderService {

    @Autowired
    private WeatherRepository weatherRepository;
    private static String city = ConfigProperties.location;
    private static final Logger logger = LoggerFactory.getLogger(DataLoaderService.class);
    private static final String url = "https://weatherapi-com.p.rapidapi.com/forecast.json?q="+city+"&days=3";

    @Scheduled(fixedDelay = 900000)
    private void updateWeather() {
        weatherRepository.deleteAll();
        List<Weather> forecast = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        headers.add("X-RapidAPI-Key", "8fe4697204msh4ab2ecdff354183p1b7fabjsne4eeecb5a69f");
        headers.add("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            JsonNode jsonNode = mapper.readTree(exchange.getBody());
            JsonNode current = jsonNode.get("current");
            Timestamp lastUpdated = new Timestamp(current.get("last_updated_epoch").asLong()*1000);
            Weather weather = createWeather(current, mapper, lastUpdated);
            forecast.add(weather);

            JsonNode forecastday = jsonNode.get("forecast").get("forecastday");

            for (JsonNode day : forecastday) {
                JsonNode hours = day.get("hour");

                for (JsonNode hour : hours) {
                    Weather fore = createWeather(hour, mapper, lastUpdated);
                    forecast.add(fore);
                }
            }

            weatherRepository.saveAll(forecast);

            logger.info("The weather's updated successfully. time : " + LocalDateTime.now());

        } catch (JsonProcessingException e) {

            logger.error("JsonProcessingException! Json has failed parse! Check serialising fields or request's data! time : " + LocalDateTime.now());
        }
    }

    private static Weather createWeather(JsonNode node, ObjectMapper mapper, Timestamp lastUpdated) throws JsonProcessingException {
        Weather weather = mapper.treeToValue(node, Weather.class);
        Timestamp date;

        if (node.findParent("time") != null){
            date = new Timestamp(node.get("time_epoch").asLong()*1000);
            weather.setStatus(WeatherStatus.FORECAST);
        } else {
            date = Timestamp.valueOf(LocalDate.now().atStartOfDay());
            weather.setStatus(WeatherStatus.CURRENT);
        }

        weather.setDate(date);
        weather.setUpdatedAt(lastUpdated);
        return weather;
    }
}
