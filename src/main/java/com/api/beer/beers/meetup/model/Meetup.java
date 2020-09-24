package com.api.beer.beers.meetup.model;

import com.api.beer.beers.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetup")
public class Meetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @OneToMany(
            mappedBy = "meetup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MeetupUser> meetupUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="creator", nullable=false)
    private User user;

    @Column(name = "beers")
    private int beers;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "duration")
    private int duration;

    @Column(name = "temp")
    private int temp;

    public Meetup() {
    }

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

    public List<MeetupUser> getMeetupUsers() {
        return meetupUsers;
    }

    public void setMeetupUsers(List<MeetupUser> meetupUsers) {
        this.meetupUsers = meetupUsers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBeers() {
        return beers;
    }

    public void setBeers(int beers) {
        this.beers = beers;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
