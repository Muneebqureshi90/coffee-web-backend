package com.example.coffee.controllers;

import com.example.coffee.dto.RatingDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.ProductException;
import com.example.coffee.expections.RatingServiceException;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.expections.UserNotFoundException;
import com.example.coffee.services.RatingService;
import com.example.coffee.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Rating  Management", description = "Rating  Detials")
@RequestMapping("/api/v1")
public class RatingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);


    @PostMapping("/rating/{userId}/{productId}")
    public ResponseEntity<RatingDto> createRating(@RequestBody RatingDto ratingDto,
                                                  @PathVariable Integer userId,
                                                  @PathVariable Integer productId) {
        try {
            RatingDto createdRating = ratingService.createRating(ratingDto, userId, productId);
            return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging
            logger.error("Error creating rating: {}", e.getMessage(), e);

            // Return an appropriate response with a specific HTTP status code
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rating/{productId}/user/{userId}")
    public ResponseEntity<List<RatingDto>> getProductsRating(@PathVariable Integer productId, @PathVariable Integer userId) {
        try {
            List<RatingDto> ratings = ratingService.findProductsRating(productId, userId);
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (RatingServiceException e) {
            // Handle RatingServiceException here
            // Return an empty list or an error list depending on your requirements
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//@GetMapping("/rating/{productId}/user/{userEmail}")
//
//public ResponseEntity<List<RatingDto>> findProductsRating(
//            @PathVariable Integer productId,
//            @PathVariable String userEmail) {
//
//        try {
//            List<RatingDto> ratings = ratingService.findProductsRating(productId, userEmail);
//            return new ResponseEntity<>(ratings, HttpStatus.OK);
//
//        } catch (UserNotFoundException e) {
//            // Handle the case where the user is not found
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        } catch (Exception e) {
//            // Handle other exceptions
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/rating/{ratingId}/user/{userId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer ratingId, @PathVariable Integer userId) {
        try {
            ratingService.deleteReview(ratingId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
