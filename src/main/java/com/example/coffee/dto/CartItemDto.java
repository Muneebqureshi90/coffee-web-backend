package com.example.coffee.dto;

import com.example.coffee.entity.Cart;
import com.example.coffee.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class CartItemDto {

    private Integer id;

    @JsonIgnore
    private Cart cart;


    private Product product;



    private Integer quantity;

    private BigDecimal price;


    private Integer discountedPrice;

    private Integer userId;
}
