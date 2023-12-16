package com.example.spring_boot_calc.entity;

import com.example.spring_boot_calc.validator.ValidDate;

import java.time.LocalDate;

public class AverageForecastDto {

    @ValidDate(message =
            "Date out of range! It has acceptable range for 3 days in advance, including today!")
    private LocalDate from;
    @ValidDate(message =
            "Date out of range! It has acceptable range for 3 days in advance, including today!")
    private LocalDate to;

    public AverageForecastDto() {
    }

    public AverageForecastDto(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
