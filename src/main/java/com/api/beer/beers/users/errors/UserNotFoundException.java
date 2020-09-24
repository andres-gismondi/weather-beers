package com.api.beer.beers.users.errors;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Exception e) {
        super(message, e);
    }

}
