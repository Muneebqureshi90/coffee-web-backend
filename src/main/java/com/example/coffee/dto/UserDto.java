package com.example.coffee.dto;

import com.example.coffee.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    private Integer id;

    @NotEmpty(message = "Please fill the name")
    @Size(min = 3, message = "Your name minimum characters should be 4")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$",
            message = "Password must be 6-20 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character (@$!%*?&)"
    )
    private String password;

    private String phoneNumber;

    private List<Address> address = new ArrayList<>();

        @JsonManagedReference("userRatingReference")
//    @JsonIgnore
//@JsonManagedReference


    private List<RatingDto> ratings = new ArrayList<>();

            @JsonManagedReference("userReviewReference")
//    @JsonManagedReference
//    @JsonIgnore
    private List<ReviewDto> reviews = new ArrayList<>();

    private RefreshToken refreshToken;

    private List<UserTwilioOtp> twilioOtps = new ArrayList<>();

    private List<EmailOtp> otps = new ArrayList<>();

    private String emailOtp;

    private String twilioOtp;

    @NotNull
    private Set<RoleDto> roles = new HashSet<>();

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    private LocalDateTime createdAt;
}
