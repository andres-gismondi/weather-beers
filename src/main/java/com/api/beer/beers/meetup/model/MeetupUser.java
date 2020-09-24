package com.api.beer.beers.meetup.model;

import com.api.beer.beers.users.model.User;

import javax.persistence.*;

@Entity
@Table(name = "meetup_user")
public class MeetupUser {

    @EmbeddedId
    private MeetupUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("meetupId")
    @JoinColumn(name = "meetup_id")
    private Meetup meetup;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "checkin")
    private Boolean checkin;

    public MeetupUser() {
    }

    public MeetupUser(Meetup meetup, User user, Boolean checkin) {
        this.meetup = meetup;
        this.user = user;
        this.checkin = checkin;
        this.id = new MeetupUserId(meetup.getId(), user.getId());
    }

    public MeetupUserId getId() {
        return id;
    }

    public void setId(MeetupUserId id) {
        this.id = id;
    }

    public Meetup getMeetup() {
        return meetup;
    }

    public void setMeetup(Meetup meetup) {
        this.meetup = meetup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getCheckin() {
        return checkin;
    }

    public void setCheckin(Boolean checkin) {
        this.checkin = checkin;
    }
}
