package com.example.coffee.dto;

import com.example.coffee.entity.Product;
import com.example.coffee.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewDto {


//    @JsonBackReference("userReviewReference")
////@JsonBackReference
//
//private UserDto user;
//
//    @JsonBackReference("productReviewReference")
////    @JsonBackReference
//    private ProductDto product;


    @JsonBackReference("userReviewReference")
    private UserDto user;

    @JsonBackReference("productReviewReference")
    private ProductDto product;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Review cannot be blank")
    @Size(min = 5, max = 1000, message = "Review must be between 5 and 1000 characters")
    private String review;

    private LocalDateTime createdAt;
}
