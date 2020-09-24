package com.api.beer.beers.users.controller;

import com.api.beer.beers.meetup.dto.MeetupDto;
import com.api.beer.beers.meetup.errors.MeetupErrorSavingException;
import com.api.beer.beers.users.dto.UserDto;
import com.api.beer.beers.users.dto.UserLogin;
import com.api.beer.beers.users.errors.UserAlreadyExistException;
import com.api.beer.beers.users.errors.UserNotFoundException;
import com.api.beer.beers.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/auth")
    public ResponseEntity<String> authentication(@RequestBody UserLogin userLogin) throws UserNotFoundException {
        String bearer =  this.usersService.getJWTToken(userLogin);
        return ResponseEntity.ok(bearer);
    }

    //Create and Update
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDto userDto) throws UserAlreadyExistException {
        this.usersService.create(userDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(new UserDto());
    }

    @PostMapping("/checkin-meetup/{user-id}")
    public ResponseEntity<HttpStatus> inscribeToMeetup(@PathVariable("user-id") String userId,@RequestParam("meetup_id") String meetupId) throws MeetupErrorSavingException, UserNotFoundException {
        this.usersService.checkinMeetup(Long.parseLong(userId), Long.parseLong(meetupId));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/meetups-created")
    public ResponseEntity<List<MeetupDto>> getMeetupsCreated(@PathVariable("id") String id) throws UserNotFoundException {
        List<MeetupDto> meetups = this.usersService.getMeetups(Long.parseLong(id));
        return ResponseEntity.ok(meetups);
    }

    @GetMapping("/{id}/meetups-inscribed")
    public ResponseEntity<List<MeetupDto>> getMeetupsInscribed(@PathVariable("id") String id) throws UserNotFoundException {
        List<MeetupDto> meetups = this.usersService.getMeetupsInscribed(Long.parseLong(id));
        return ResponseEntity.ok(meetups);
    }

    @GetMapping("/{id}/meetups-checkin")
    public ResponseEntity<List<MeetupDto>> getCheckinMeetups(@PathVariable("id") String id) throws UserNotFoundException {
        List<MeetupDto> meetups = this.usersService.getCheckinMeetups(Long.parseLong(id));
        return ResponseEntity.ok(meetups);
    }

}
