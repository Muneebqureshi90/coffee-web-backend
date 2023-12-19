package com.example.coffee.serviceImpl;

import com.example.coffee.dto.RatingDto;
import com.example.coffee.entity.Product;
import com.example.coffee.entity.Rating;
import com.example.coffee.entity.User;

import com.example.coffee.expections.RatingServiceException;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.ProductDto;
import com.example.coffee.repository.RatingRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.RatingService;
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
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductDto productRepository;
    private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);



    @Override
    @Transactional

    public RatingDto createRating(RatingDto ratingDto, Integer userId, Integer productId) {
        try {
            // Check if the user has already rated the product
            boolean hasAlreadyRated = ratingRepository.existsByUser_IdAndProduct_Id(userId, productId);
            if (hasAlreadyRated) {
                // Throw an exception or handle the case where the user has already rated the product
                throw new RuntimeException("User has already rated the product.");
            }

            // Map the RatingDto to the Rating entity
            Rating rating = modelMapper.map(ratingDto, Rating.class);

            // Fetch the User entity based on the provided User ID
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

            // Fetch the Product entity based on the provided Product ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

            // Set the User and Product for the Rating
            rating.setUser(user);
            rating.setProduct(product);
            rating.setCreatedAt(LocalDateTime.now());

            // Save the Rating entity
            rating = ratingRepository.save(rating);

            // Map the saved entity back to a RatingDto and return it
            return modelMapper.map(rating, RatingDto.class);
        } catch (DataAccessException ex) {
            // Log the exception for debugging
            logger.error("Error saving rating data: {}", ex.getMessage(), ex);

            // Throw a custom exception or handle it as appropriate
            throw new RuntimeException("Error saving rating data: " + ex.getMessage(), ex);
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
    public List<RatingDto> findProductsRating(Integer productId, Integer userId) throws RatingServiceException {
        try {
            // Assuming you have a method in your repository to fetch ratings by product and user
            List<Rating> ratings = ratingRepository.getAllProductRatingByUser(productId, userId);

            // Map the list of Rating entities to a list of RatingDto objects
            List<RatingDto> ratingDtos = ratings.stream()
                    .map(rating -> modelMapper.map(rating, RatingDto.class))
                    .collect(Collectors.toList());

            return ratingDtos;
        } catch (Exception e) {
            // Handle the exception here, you can log the error or rethrow a custom exception
            logger.error("Error fetching product ratings: {}", e.getMessage(), e);
            throw new RatingServiceException("An error occurred while fetching product ratings", e);
        }
    }


//    @Override
//    @Transactional
//    public List<RatingDto> findProductsRating(Integer productId, String userEmail) {
//        try {
//            // Find the user by email using the UserRepository
//            Optional<User> userOptional = userRepository.findByEmail(userEmail);
//
//            // Use orElseThrow to get the User or throw UserNotFoundException
//            User user = userOptional.orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
//
//            // Assuming you have a method in your repository to fetch ratings by product and user ID
//            List<Rating> ratings = ratingRepository.getAllProductRatingByUser(productId, user.getId());
//
//            // Map the list of Rating entities to a list of RatingDto objects
//            List<RatingDto> ratingDtos = ratings.stream()
//                    .map(rating -> modelMapper.map(rating, RatingDto.class))
//                    .collect(Collectors.toList());
//
//            return ratingDtos;
//        } catch (UserNotFoundException e) {
//            // Handle the case where the user is not found
//            logger.error("User not found with email: {}", userEmail);
//            throw new UserNotFoundException("User not found while fetching product ratings");
//        }
//    }





    @Override
    public void deleteReview(Integer ratingId, Integer userId) {
        // Check if the review exists and is associated with the provided userId
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        if (rating.isPresent() && rating.get().getUser().getId().equals(userId)) {
            ratingRepository.deleteById(ratingId);
        } else {
            throw new ResourceNotFoundException("Review with id " + ratingId + " not found for user with id " + userId);
        }
    }

}
