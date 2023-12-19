package com.example.coffee.dto;

import com.example.coffee.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserTwilioOtpDto {

    private Long id;

    @NotBlank(message = "OTP cannot be blank")
    private String otp;

    @NotNull(message = "Creation time cannot be null")
    private LocalDateTime creationTime;



    // You may want to validate the User object as well, depending on your requirements
    @NotNull(message = "User cannot be null")
    private UserDto user;
}
