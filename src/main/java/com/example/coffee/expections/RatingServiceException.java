package com.example.coffee.expections;

public class RatingServiceException extends Exception {

    public RatingServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
