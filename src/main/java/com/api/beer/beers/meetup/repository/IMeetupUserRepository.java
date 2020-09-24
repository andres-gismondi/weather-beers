package com.api.beer.beers.meetup.repository;

import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.meetup.model.MeetupUser;
import com.api.beer.beers.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMeetupUserRepository extends JpaRepository<MeetupUser, Long> {

    Optional<MeetupUser> findMeetupUserByUserAndMeetup(User user, Meetup meetup);

}
