package com.example.coffee.dto;

import com.example.coffee.entity.Order;
import com.example.coffee.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class AddressDto {

    private Integer id;

    @NotBlank(message = "Street address is required")
    private String streetAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Size(min = 5, max = 10, message = "Zip code should be between 5 and 10 characters")
    private String zipCode;

    //    @NotNull(message = "User is required")
//    @JsonBackReference("userRatingReference")
    private UserDto userDto;
    private OrderDto orderDto;

}
