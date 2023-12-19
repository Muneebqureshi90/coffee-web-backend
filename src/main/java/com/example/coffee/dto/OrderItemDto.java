package com.example.coffee.dto;

import com.example.coffee.entity.Order;
import com.example.coffee.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private Integer id;


    private Order order;


    private Product product;

    private String size;

    private Integer quantity;

    private Integer price;

    private Integer discountedPrice;

    private Integer userId;

    private LocalDateTime deliveryDate;
}
