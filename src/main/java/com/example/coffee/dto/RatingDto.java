package com.example.coffee.dto;

import com.example.coffee.entity.Product;
import com.example.coffee.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RatingDto {
    private Integer r_id;

//
////    @JsonBackReference
//    private UserDto user;
//
////    @JsonBackReference
////    @JsonIgnore
//    private ProductDto product;



    @JsonBackReference("userRatingReference")
    private UserDto user;

    @JsonBackReference("productRatingReference")
    private ProductDto product;


    private double rating;

    private LocalDateTime createdAt;
}
