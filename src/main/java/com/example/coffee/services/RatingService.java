package com.example.coffee.services;

import com.example.coffee.dto.RatingDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.RatingServiceException;

import java.util.List;

public interface RatingService {
    RatingDto createRating(RatingDto ratingDto, Integer userId, Integer productId);

    List<RatingDto> findProductsRating(Integer productId, Integer userId)  throws RatingServiceException;

    void deleteReview(Integer ratingId, Integer userId);
}

