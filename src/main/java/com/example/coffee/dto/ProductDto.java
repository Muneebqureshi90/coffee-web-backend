package com.example.coffee.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDto {

    private Integer p_id;
    @JsonManagedReference("productReviewReference")
//    @JsonIgnore
//    @JsonBackReference

    private List<ReviewDto> reviews = new ArrayList<>();

    @JsonManagedReference("productRatingReference")
//    @JsonIgnore
//    @JsonBackReference

    private List<RatingDto> ratings = new ArrayList<>();

    @JsonBackReference("userProductReference")
    private OrderDto order;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String discription;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;

    @NotNull(message = "Discount amount cannot be null")
    @Positive(message = "Discount amount must be a positive value")
    private BigDecimal discount_amount;

    @NotNull(message = "Category cannot be null")
    @JsonBackReference("categoryReference")

    private CategoryDto category;

    private LocalDateTime createdAt;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive value")
    private Integer quantity;
}
