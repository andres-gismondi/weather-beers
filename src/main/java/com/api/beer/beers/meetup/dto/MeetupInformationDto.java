package com.api.beer.beers.meetup.dto;

public class MeetupInformationDto {

    private int temp;
    private int beers;
    private String description;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getBeers() {
        return beers;
    }

    public void setBeers(int beers) {
        this.beers = beers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
