package com.api.beer.beers.meetup.repository;

import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMeetupRepository extends JpaRepository<Meetup, Long> {

    Optional<Meetup> findMeetupById(Long id);

    Optional<Meetup> findMeetupByDateAndName(LocalDateTime date, String name);

    List<Meetup> findAllByUser(User user);

}
