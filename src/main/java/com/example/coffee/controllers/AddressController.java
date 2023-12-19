package com.example.coffee.controllers;

import com.example.coffee.dto.AddressDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.expections.ApiResponse;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.services.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Address Controller", description = "This is Address Controller")
public class AddressController {

    @Autowired
    private AddressService addressService;

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    // AddressController
    @PostMapping("/user/{userId}/order/{orderId}/address")
    public ResponseEntity<AddressDto> createAddress(
            @PathVariable Integer userId,
            @PathVariable Integer orderId,
            @Valid @RequestBody AddressDto addressDto
    ) {
        try {
            // Instantiate a new UserDto and set the userId
            UserDto userDto = new UserDto();
            userDto.setId(userId);

            // Set the userDto in the AddressDto
            addressDto.setUserDto(userDto);

            AddressDto createdAddressDto = addressService.createAddress(addressDto, userId, orderId);
            return new ResponseEntity<>(createdAddressDto, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            // Log the exception for debugging
            logger.error("Error creating address: {}", e.getMessage(), e);

            // Rethrow the exception or handle it as appropriate
            throw new RuntimeException("Error creating address: " + e.getMessage(), e);
        }
    }


    @PutMapping("/address/{id}")
    public ResponseEntity<AddressDto> updateAddress(@Valid @PathVariable("id") Integer id, @RequestBody AddressDto addressDto) {
        try {
            AddressDto updatedAddressDto = addressService.updateAddress(id, addressDto);
            return new ResponseEntity<>(updatedAddressDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Handle the case where the address is not found, e.g., return a 404 Not Found response
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle other exceptions, e.g., return a 500 Internal Server Error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable("id") Integer id) {
        addressService.deleteAddress(id);
        String status = "OK"; // Set the appropriate status
        String time = "current_timestamp"; // Set the appropriate timestamp

        ApiResponse apiResponse = new ApiResponse("Address Deleted", true, status, time);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressDto>> getAllAddress(AddressDto addressDto) {
        return ResponseEntity.ok(this.addressService.getAllAddresses());
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable("id") Integer id) {
        try {
            Optional<AddressDto> optionalAddressDto = addressService.getAddressById(id);

            if (optionalAddressDto.isPresent()) {
                return new ResponseEntity<>(optionalAddressDto.get(), HttpStatus.OK);
            } else {
                // Handle the case where the address is not found, e.g., return a 404 Not Found response
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions, e.g., return a 500 Internal Server Error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/byOrder/{orderId}")
    public ResponseEntity<Page<AddressDto>> getAddressesByOrder(
            @PathVariable Integer orderId,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        try {
            Page<AddressDto> addresses = addressService.getAddressesByOrder(orderId, pageable);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Handle the exception and return an appropriate response, e.g., 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<Page<AddressDto>> getAddressesByUser(
            @PathVariable Integer userId,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        try {
            Page<AddressDto> addresses = addressService.getAddressesByOrder(userId, pageable);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Handle the exception and return an appropriate response, e.g., 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
