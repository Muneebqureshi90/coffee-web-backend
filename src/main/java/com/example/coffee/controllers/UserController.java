package com.example.coffee.controllers;

import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ApiResponse;
import com.example.coffee.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
@Tag(name = "User Controller", description = "This is User Controller")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            UserDto user = this.userService.createUser(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer userId) {
        try {
            // Check if the user exists
            UserDto existingUser = this.userService.getUserById(userId);
            if (existingUser == null) {
                throw new RuntimeException("User Id Not Found: " + userId);
            }

            // Perform the logic to update the user with the provided data (not implemented in your code)
            // Example: userService.updateUser(userDto);

            // Return the updated user (you might want to return the updated userDto after the update)
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            // Handle exceptions appropriately based on your application's requirements
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId) {
        this.userService.deleteUser(userId);
        String status = "OK"; // Set the appropriate status
        String time = "current_timestamp"; // Set the appropriate timestamp

        ApiResponse apiResponse = new ApiResponse("User Deleted", true, status, time);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@Valid @PathVariable("userId") Integer userId) {
        try {
            UserDto user = this.userService.getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
