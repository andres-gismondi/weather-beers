package com.api.beer.beers.meetup.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MeetupUserId implements Serializable {

    @Column(name = "meetup_id")
    private Long meetupId;

    @Column(name = "user_id")
    private Long userId;

    public MeetupUserId() {
    }

    public MeetupUserId(Long meetupId, Long userId) {
        this.meetupId = meetupId;
        this.userId = userId;
    }

    public Long getMeetupId() {
        return meetupId;
    }

    public void setMeetupId(Long meetupId) {
        this.meetupId = meetupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetupUserId that = (MeetupUserId) o;
        return Objects.equals(meetupId, that.meetupId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetupId, userId);
    }
}
