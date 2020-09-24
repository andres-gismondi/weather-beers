package com.api.beer.beers.meetup.errors;

public class MeetupNotFoundException extends Exception{

    public MeetupNotFoundException(String message) {
        super(message);
    }

}
