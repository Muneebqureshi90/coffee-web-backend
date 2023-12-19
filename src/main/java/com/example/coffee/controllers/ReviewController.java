package com.example.coffee.controllers;


import com.example.coffee.dto.ReviewDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.ProductException;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.expections.ReviewServiceException;
import com.example.coffee.services.ReviewService;
import com.example.coffee.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Review  Management", description = "Review  Detials")
@RequestMapping("/api/v1")
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/review/{userId}/{productId}")
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto,
                                                  @PathVariable Integer userId,
                                                  @PathVariable Integer productId) {
        try {
            ReviewDto createdReview = reviewService.createReview(reviewDto, userId, productId);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Handle the exception and return an appropriate response
            // You may also want to log the exception for debugging purposes
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/review/{productId}/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getProductsReview(@PathVariable Integer productId, @PathVariable Integer userId) {
        try {
            List<ReviewDto> reviews = reviewService.getProductsReview(productId, userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ReviewServiceException e) {
            // Log the exception if needed
//            logger.error("Error fetching product reviews: {}", e.getMessage(), e);
            // Return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/review/{reviewId}/user/{userId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        try {
            reviewService.deleteReview(reviewId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}