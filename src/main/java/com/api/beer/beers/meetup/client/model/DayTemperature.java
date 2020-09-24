package com.api.beer.beers.meetup.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DayTemperature {

    private MainTemperature main;
    private List<WeatherTemperature> weather;

    public MainTemperature getMain() {
        return main;
    }

    public void setMain(MainTemperature main) {
        this.main = main;
    }

    public List<WeatherTemperature> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherTemperature> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "DayTemperature{" +
                "main=" + main +
                ", weather=" + weather +
                '}';
    }
}
