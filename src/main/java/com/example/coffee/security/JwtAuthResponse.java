package com.example.coffee.security;


import com.example.coffee.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class JwtAuthResponse {
    private String token;

    private UserDto user;
    //    This is for refresh Token
    private String refreshToken;
//    private String userName;


    //     for Otp on emial
    private boolean otpSent;
    private boolean otpVerificationRequired;
}
