package com.example.coffee.dto;

import com.example.coffee.entity.Address;
import com.example.coffee.entity.Product;
import com.example.coffee.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {

    @NotNull(message = "Order ID cannot be null")
    private Integer id;

    @NotBlank(message = "Order ID cannot be blank")   private String orderId;

    @Valid // Ensures that the UserDto is also validated
    @NotNull(message = "User cannot be null")
    private UserDto user;

    @Valid // Ensures that each ProductDto in the list is also validated
    @NotEmpty(message = "Products list cannot be empty")
    @JsonManagedReference("userProductReference")

    private List<@Valid ProductDto> products = new ArrayList<>();

    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;

    @NotNull(message = "Delivery date cannot be null")
    private LocalDateTime deliveryDate;

    @Valid // Ensures that the AddressDto is also validated
//    @NotNull(message = "Shipping address cannot be null")
    private AddressDto shippingAddress;

    @DecimalMin(value = "0.0", message = "Total price must be a positive number")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0.0", message = "Total discounted price must be a positive number")
    private BigDecimal totalDiscountedPrice;

    @NotBlank(message = "Order status cannot be blank")
    private String orderStatus;

    @Min(value = 0, message = "Total items must be a non-negative number")
    private Integer totalItems;

    @NotNull(message = "Created at date cannot be null")
    private LocalDateTime createdAt;
}
