package com.example.coffee.controllers;


import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.RefreshToken;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.expections.UserNotFoundException;
import com.example.coffee.security.JwtAuthRequest;
import com.example.coffee.security.JwtAuthResponse;
import com.example.coffee.security.JwtTokenHelper;
import com.example.coffee.security.RefreshTokenRequest;
import com.example.coffee.services.RefreshTokenService;
import com.example.coffee.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "This is Authentication Controller")

public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenHelper helper;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

//    @PostMapping("/login")
//    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {
//        this.doAuthenticate(request.getUsername(), request.getPassword());
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//        String token = this.helper.generateToken(userDetails);
//
//        // This is for Refresh Token
//        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//        // Populate additional user-related information in the response
//        JwtAuthResponse response = new JwtAuthResponse();
//        response.setToken(token);
//        response.setRefreshToken(refreshToken.getRefreshToken());
////        response.setUser(userDetails.getUsername());
////        response.setUser(userDetails.getUsername());  // Add other user-related fields as needed
//        response.setUser(this.modelMapper.map((User)userDetails,UserDto.class));
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {
        try {
            System.out.println("Received request: " + request);
            this.doAuthenticate(request.getUserName(), request.getPassword());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
            String token = this.helper.generateToken(userDetails);
            System.out.println(userDetails);

            // This is for Refresh Token
            RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());

            JwtAuthResponse response = new JwtAuthResponse();
            response.setToken(token);
            response.setRefreshToken(refreshToken.getRefreshToken());
            response.setUser(this.modelMapper.map((User)userDetails, UserDto.class));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception or handle it as appropriate for your application
            // For demonstration purposes, printing the stack trace to console
            e.printStackTrace();

            // You can customize the response based on the type of exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // AuthController.java

//    @PostMapping("/login")
//    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {
//        try {
//            // Authenticate the user
//            this.doAuthenticate(request.getUserName(), request.getPassword());
//
//            // Load user details and generate JWT token
//            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
//            String token = this.helper.generateToken(userDetails);
//
//            // Create a refresh token
//            RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Send login OTP after successful login
//            try {
//                userService.sendLoginOtp(request.getUserName());
//
//                // If OTP is sent successfully, include OTP verification information in the response
//                JwtAuthResponse response = new JwtAuthResponse();
//                response.setToken(token);
//                response.setRefreshToken(refreshToken.getRefreshToken());
//                response.setOtpSent(true); // Include OTP information
//                response.setOtpVerificationRequired(true); // Indicate that OTP verification is required
//
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } catch (UserNotFoundException e) {
//                // Handle the case where the user is not found (optional)
//                logger.error("User not found after successful login");
//
//                // Return a response without OTP information
//                JwtAuthResponse response = new JwtAuthResponse();
//                response.setToken(token);
//                response.setRefreshToken(refreshToken.getRefreshToken());
//                response.setOtpSent(false);
//                response.setOtpVerificationRequired(false);
//
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } catch (Exception e) {
//                // Handle other exceptions, such as email sending failure
//                logger.error("Failed to send login OTP after successful login", e);
//
//                // Return a response without OTP information
//                JwtAuthResponse response = new JwtAuthResponse();
//                response.setToken(token);
//                response.setRefreshToken(refreshToken.getRefreshToken());
//                response.setOtpSent(false);
//                response.setOtpVerificationRequired(false);
//
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        } catch (BadCredentialsException e) {
//            // Handle invalid credentials
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    private void doAuthenticate(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new BadCredentialsException("Username is null or empty");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);

        try {
            this.authenticationManager.authenticate(authentication);
            // Log successful authentication
            logger.info("User {} successfully authenticated.", username);
        } catch (BadCredentialsException e) {
            // Log authentication failure
            logger.error("Authentication failed for user {}: {}", username, e.getMessage());
            throw new BadCredentialsException("Invalid Username or Password!!");
        }
    }



    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid!!";
    }

    @PostMapping("/send-login-otp")
    public ResponseEntity<String> sendLoginOtp(@RequestParam("email") String email) {
        try {
            userService.sendLoginOtp(email);
            return new ResponseEntity<>("Login OTP sent successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-login-otp")
    public ResponseEntity<String> verifyLoginOtp(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp) {
        try {
            boolean isOtpValid = userService.verifyLoginOtp(email, otp);

            if (isOtpValid) {
                return new ResponseEntity<>("Login OTP verified successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid OTP or OTP expired", HttpStatus.BAD_REQUEST);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    Register and also controller of otps

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            UserDto userRegister = this.userService.registerUser(userDto);
            return new ResponseEntity<>(userRegister, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception or handle it as appropriate for your application
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmailOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean isOtpValid = userService.verifyEmailOtp(email, otp);
            if (isOtpValid) {
                return new ResponseEntity<>("Email OTP verified successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid Email OTP", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error verifying Email OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            userService.sendForgetPasswordEmail(email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (ResourceNotFoundException e) {
            // Handle the case where the user with the provided email is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (RuntimeException e) {
            // Handle other exceptions, such as email sending failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send password reset email.");
        }
    }

    // Additional endpoint to handle password reset link click
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (ResourceNotFoundException e) {
            // Handle the case where the user associated with the token is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> verifyForgetPasswordOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        try {
            boolean otpVerified = userService.verifyForgetPasswordOtp(email, otp);

            if (otpVerified) {
                return ResponseEntity.ok("OTP verification successful. Proceed to reset password.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP. Please try again.");
            }
        } catch (ResourceNotFoundException e) {
            // Handle the case where the user with the provided email is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify OTP.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetUserPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword) {

        try {
            userService.resetPassword(email, newPassword);
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/regenerate-email-otp")
    public ResponseEntity<String> regenerateEmailOtp(@RequestParam("email") String email) {
        try {
            String newOtp = userService.regenerateEmailOtp(email);
            return new ResponseEntity<>("New Email OTP generated successfully: " + newOtp, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping("/verify-twilio")
//    public ResponseEntity<String> verifyTwilioOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
//        try {
//            boolean isOtpValid = userService.verifyTwilioOtp(phoneNumber, otp);
//            if (isOtpValid) {
//                return new ResponseEntity<>("Twilio OTP verified successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Invalid Twilio OTP", HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error verifying Twilio OTP", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/register")
//    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
//        // Create a new user
//        UserDto registeredUser = this.userService.registerNewUser(userDto);
//
//        // Create a new cart for the user
//        CartDto cartDto = new CartDto();
//        cartDto.setUserId(registeredUser.getId()); // Set the user ID in the cartDto
//
//        CartDto createdCart = this.cartService.createCart(registeredUser); // Pass the registeredUser instead of cartDto
//
//        // Set the created cart ID to the user
//        registeredUser.setCartId(createdCart.getId());
//
//        // Update the user with the cart ID
//        this.userService.updateUser(registeredUser, registeredUser.getId());
//
//        // Update the userDto with the createdAt
//        registeredUser.setCreateAt(LocalDateTime.now()); // Use setCreateAt() instead of setCreatedAt()
//
//        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
//    }


    // Refresh Token
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        try {
            // Verify the refresh token and retrieve the associated user
            RefreshToken refreshToken = this.refreshTokenService.verifyRefreshToken(request.getRefreshToken());
            User user = refreshToken.getUser();

            // Generate a new JWT token for the user
            String newToken = this.helper.generateToken(user);

            // Create a response containing the new JWT token
            JwtAuthResponse response = new JwtAuthResponse();
            response.setToken(newToken);

            // Return the response to the client
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Handle exceptions thrown during token refresh, e.g., token expired or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
