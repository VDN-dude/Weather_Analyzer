package com.example.spring_boot_calc.entity;

import com.example.spring_boot_calc.config.ConfigProperties;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "weather")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"temp_c", "wind_mph", "pressure_mb", "humidity", "condition", "location"})
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(name = "location")
    private final String location = ConfigProperties.location;
    @Column(name = "temp_c")
    @JsonProperty("tempC")
    private Double tempC;
    @Column(name = "wind_mph")
    @JsonProperty("windMph")
    private Double windMph;
    @Column(name = "pressure_mb")
    @JsonProperty("pressureMb")
    private Double pressureMb;
    @Column(name = "humidity")
    private Double humidity;
    @Column(name = "condition")
    private String condition;
    @Column(name = "date")
    @JsonIgnore
    private Timestamp date;
    @Column(name = "updated_at")
    @JsonIgnore
    private Timestamp updatedAt;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private WeatherStatus status;

    public Weather() {
    }

    public Weather(Long id, Double tempC, Double windMph, Double pressureMb, Double humidity, String condition, Timestamp date, Timestamp updatedAt, WeatherStatus status) {
        this.id = id;
        this.tempC = tempC;
        this.windMph = windMph;
        this.pressureMb = pressureMb;
        this.humidity = humidity;
        this.condition = condition;
        this.date = date;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    @JsonCreator
    public Weather(@JsonProperty("temp_c") Double tempC,
                   @JsonProperty("wind_mph") Double windMph,
                   @JsonProperty("pressure_mb") Double pressureMb,
                   @JsonProperty("humidity") Double humidity,
                   @JsonProperty("condition") JsonNode condition
    ) {
        this.tempC = tempC;
        this.windMph = windMph;
        this.pressureMb = pressureMb;
        this.humidity = humidity;
        this.condition = condition.get("text").asText();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTempC() {
        return tempC;
    }

    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    public Double getWindMph() {
        return windMph;
    }

    public void setWindMph(Double windMph) {
        this.windMph = windMph;
    }

    public Double getPressureMb() {
        return pressureMb;
    }

    public void setPressureMb(Double pressureMb) {
        this.pressureMb = pressureMb;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getCondition() {
        return condition;
    }

    public WeatherStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public void setStatus(WeatherStatus status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", tempC=" + tempC +
                ", windMph=" + windMph +
                ", pressureMb=" + pressureMb +
                ", humidity=" + humidity +
                ", condition='" + condition + '\'' +
                ", date=" + date +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}
