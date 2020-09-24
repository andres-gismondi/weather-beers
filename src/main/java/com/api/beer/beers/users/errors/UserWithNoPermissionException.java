package com.api.beer.beers.users.errors;

public class UserWithNoPermissionException extends Exception{

    public UserWithNoPermissionException(String message) {
        super(message);
    }

}
