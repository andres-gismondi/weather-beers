package com.api.beer.beers.meetup.errors;

import com.api.beer.beers.users.errors.UserAlreadyExistException;

public class UserAssociatedException extends Exception{

    public UserAssociatedException(String message) {
        super(message);
    }

}
