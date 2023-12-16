package com.example.spring_boot_calc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDateTimeParseException(DateTimeParseException e) {
        Map<String, String> map = new HashMap<>();
        map.put("code", HttpStatus.BAD_REQUEST.toString());
        map.put("message", "Date entered incorrect!" +
                " It should be in the format yyyy-mm-dd!" +
                " Please try again.");
        map.put("timestamp", LocalDateTime.now().toString());
        return map;
    }
}
