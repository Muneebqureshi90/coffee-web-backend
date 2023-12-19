package com.example.coffee.services;

import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.UserException;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    UserDto createUser(UserDto userDto) throws UserException;

    List<UserDto> getAllUsers();

    void deleteUser(Integer userId);

    UserDto updateUser(UserDto userDto, Integer userId);

    UserDto getUserById(Integer userId);

    // Email OTP methods


    boolean verifyEmailOtp(String email, String otp);

    // Twilio OTP methods


//    boolean verifyTwilioOtp(String phoneNumber, String otp);

    // Forget Password methods
    void sendForgetPasswordEmail(String email);

    boolean verifyForgetPasswordOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    // Regenerate OTP methods
    String regenerateEmailOtp(String email);

//    String regenerateTwilioOtp(String phoneNumber);

    // Login OTP methods
    void sendLoginOtp(String email);

    boolean verifyLoginOtp(String email, String otp);

    // Signup OTP methods

}
