package com.example.spring_boot_calc.entity;

import com.example.spring_boot_calc.config.ConfigProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class AverageForecast {

    @JsonIgnore
    private Timestamp from;
    @JsonIgnore
    private Timestamp to;
    private Double avgTempC;
    private static final String location = ConfigProperties.location;

    public AverageForecast() {
    }

    public AverageForecast(Timestamp from, Timestamp to, Double avgTempC) {
        this.from = from;
        this.to = to;
        this.avgTempC = avgTempC;
    }

    public Timestamp getFrom() {
        return from;
    }

    public void setFrom(Timestamp from) {
        this.from = from;
    }

    public Timestamp getTo() {
        return to;
    }

    public void setTo(Timestamp to) {
        this.to = to;
    }

    public Double getAvgTempC() {
        return avgTempC;
    }

    public void setAvgTempC(Double avgTempC) {
        this.avgTempC = avgTempC;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "AverageForecast{" +
                "from=" + from +
                ", to=" + to +
                ", avgTempC=" + avgTempC +
                '}';
    }
}
