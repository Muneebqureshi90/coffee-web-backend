package com.example.coffee.expections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

//    For User Class

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        String message = ex.getMessage();
        boolean success = false; // Set to true or false based on your logic
        String status = "NOT_FOUND"; // Set the appropriate status
        String time = "current_timestamp"; // Set the appropriate timestamp

        // Create an ApiResponse object with the additional fields
        ApiResponse apiResponse = new ApiResponse(message, success, status, time);


        // Create a ResponseEntity with the ApiResponse object and HttpStatus.NOT_FOUND
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }


//    For Validating

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethadArgsNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> resp = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            resp.put(fieldName, errorMessage);
        });


        return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
    }
}

