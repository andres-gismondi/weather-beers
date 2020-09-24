package com.api.beer.beers.meetup.controller;

import com.api.beer.beers.meetup.dto.MeetupDto;
import com.api.beer.beers.meetup.dto.MeetupInformationDto;
import com.api.beer.beers.meetup.errors.MeetupAlreadyExistException;
import com.api.beer.beers.meetup.errors.MeetupErrorSavingException;
import com.api.beer.beers.meetup.errors.UserAssociatedException;
import com.api.beer.beers.meetup.service.MeetupService;
import com.api.beer.beers.users.errors.UserNotFoundException;
import com.api.beer.beers.users.errors.UserWithNoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "meetup")
public class MeetupController {

    private static Logger logger = LoggerFactory.getLogger(MeetupController.class);

    @Autowired
    private MeetupService meetupService;

    //Create and Update
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeetupDto meetupDto) throws MeetupAlreadyExistException, IOException, UserNotFoundException, MeetupErrorSavingException {
        this.meetupService.create(meetupDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/add-user/{meetup-id}")
    public ResponseEntity<HttpStatus> addUser(@PathVariable("meetup-id") String meetupId, @RequestParam("user-id") String userId) throws UserNotFoundException, MeetupErrorSavingException, UserAssociatedException {
        this.meetupService.addUserToMeetup(Long.parseLong(meetupId), Long.parseLong(userId));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetupDto> getMeetup(@PathVariable("id") String id) {
        MeetupDto meetupDto = this.meetupService.getMeetup(Long.valueOf(id));
        return ResponseEntity.ok(meetupDto);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<MeetupDto>> getMeetups(@RequestParam("user_id") String userId) throws  UserNotFoundException {
        List<MeetupDto> meetupDto = this.meetupService.getMeetups(Long.valueOf(userId));
        return ResponseEntity.ok(meetupDto);
    }

    @GetMapping("/temperature-and-beers")
    public ResponseEntity<MeetupInformationDto> getTempAndBeers(@RequestParam("meetup_id") String meetupId) throws IOException, UserNotFoundException, UserWithNoPermissionException {
        MeetupInformationDto responses = this.meetupService.getTempAndBeerInformation(Long.valueOf(meetupId));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/temperature")
    public ResponseEntity<Integer> getTemperature(@RequestParam("meetup_id") String meetupId) throws IOException {
        int temperature = this.meetupService.getTemperature(Long.valueOf(meetupId));
        return ResponseEntity.ok(temperature);
    }

}
