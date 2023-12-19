package com.example.coffee.expections;

public class ReviewServiceException extends Exception {

    public ReviewServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
