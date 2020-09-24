package com.api.beer.beers.meetup.errors;

public class MeetupAlreadyExistException extends Exception{

    public MeetupAlreadyExistException(String message) {
        super(message);
    }
}
