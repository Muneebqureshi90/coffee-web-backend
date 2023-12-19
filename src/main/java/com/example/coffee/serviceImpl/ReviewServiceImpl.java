package com.example.coffee.serviceImpl;

import com.example.coffee.dto.RatingDto;
import com.example.coffee.dto.ReviewDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.Product;
import com.example.coffee.entity.Rating;
import com.example.coffee.entity.Review;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ProductException;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.expections.ReviewServiceException;
import com.example.coffee.repository.ProductDto;
import com.example.coffee.repository.ReviewRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.ReviewService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductDto productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto, Integer userId, Integer productId) {
        try {
            // Check if the user has already reviewed the product
            boolean hasAlreadyReviewed = reviewRepository.existsByUser1_IdAndProduct_Id(userId, productId);
            if (hasAlreadyReviewed) {
                // Throw an exception or handle the case where the user has already reviewed the product
                throw new RuntimeException("User has already reviewed the product.");
            }

            // Map the ReviewDto to the Review entity
            Review review = modelMapper.map(reviewDto, Review.class);

            // Fetch the User entity based on the provided User ID
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

            // Fetch the Product entity based on the provided Product ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

            // Set the User and Product for the Review
            review.setUser(user);
            review.setProduct(product);
            review.setCreatedAt(LocalDateTime.now());

            // Save the Review entity
            review = reviewRepository.save(review);

            // Map the saved entity back to a ReviewDto and return it
            return modelMapper.map(review, ReviewDto.class);
        } catch (DataAccessException ex) {
            // Log the exception for debugging
            logger.error("Error saving review data: {}", ex.getMessage(), ex);

            // Throw a custom exception or handle it as appropriate
            throw new RuntimeException("Error saving review data: " + ex.getMessage(), ex);
        } catch (ResourceNotFoundException ex) {
            // Log the exception for debugging
            logger.error("Error fetching user or product data: {}", ex.getMessage(), ex);

            // Rethrow the exception or handle it as appropriate
            throw ex;
        } catch (Exception ex) {
            // Log the exception for debugging
            logger.error("Unexpected error: {}", ex.getMessage(), ex);

            // Throw a custom exception or handle it as appropriate
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }


    @Override
    @Transactional
    public List<ReviewDto> getProductsReview(Integer productId, Integer userId) throws ReviewServiceException {
        try {
            // Assuming you have a method in your repository to fetch reviews by product and user
            List<Review> reviews = reviewRepository.getAllProductReviewByUser(productId, userId);

            // Map the list of Review entities to a list of ReviewDto objects
            List<ReviewDto> reviewDtos = reviews.stream()
                    .map(review -> modelMapper.map(review, ReviewDto.class))
                    .collect(Collectors.toList());

            return reviewDtos;
        } catch (Exception e) {
            // Handle the exception here, you can log the error or rethrow a custom exception
            logger.error("Error fetching product reviews: {}", e.getMessage(), e);
            throw new ReviewServiceException("An error occurred while fetching product reviews", e);
        }
    }


    @Override
    public void deleteReview(Integer reviewId, Integer userId) throws ResourceNotFoundException {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isPresent() && review.get().getUser().getId().equals(userId)) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new ResourceNotFoundException("Review with id " + reviewId + " not found for user with id " + userId);
        }
    }
}
