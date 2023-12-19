package com.example.coffee.services;//package com.example.ecommerce.services;



import com.example.coffee.dto.ReviewDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.ReviewServiceException;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewDto reviewDto, Integer userId, Integer productId);

    List<ReviewDto> getProductsReview(Integer productId, Integer userId) throws ReviewServiceException;
    void deleteReview(Integer reviewId, Integer userId);

}
