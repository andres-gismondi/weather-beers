package com.api.beer.beers.meetup.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty(value = "list")
    List<DayTemperature> days;

    public List<DayTemperature> getDays() {
        return days;
    }

    public void setDays(List<DayTemperature> days) {
        this.days = days;
    }
}
