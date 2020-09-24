package com.api.beer.beers.meetup.dto;

import java.time.LocalDateTime;

public class MeetupDto {

    private Long id;
    private String name;
    private String city;
    private LocalDateTime date;
    private int duration;
    private Long creatorId;
    private int enrolled;
    private int beers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public int getBeers() {
        return beers;
    }

    public void setBeers(int beers) {
        this.beers = beers;
    }
}
