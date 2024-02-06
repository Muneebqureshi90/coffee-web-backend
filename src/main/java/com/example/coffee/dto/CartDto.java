package com.example.coffee.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties({"user", "cart"}) // Add this annotation to ignore specific properties causing circular reference

@NoArgsConstructor
public class CartDto {

    private Integer id;

    private Integer userId;
    private Integer productId;
//    @JsonBackReference("usercartReference")
//
//    private UserDto user;

    //    orphanRemoval = true  if some one delete the item  than it will delete from database

    private Set<CartItemDto> cartItems = new HashSet<>(); // Change CartItem to CartItemDto

    private double totalPrice;

    private Integer totalItems;

    private Integer totalDiscountedPrice;
    private Integer discounte;

    private String size;

    private Integer quantity;
    private Integer price;



}
