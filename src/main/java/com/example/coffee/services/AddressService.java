package com.example.coffee.services;

import com.example.coffee.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    // Create
    AddressDto createAddress(AddressDto addressDto,Integer userId,Integer categoryId);

    // Read
    Optional<AddressDto> getAddressById(Integer id);

    List<AddressDto> getAllAddresses();

    Page<AddressDto> getAddressesByOrder(Integer orderId, Pageable pageable);

    Page<AddressDto> getAddressesByUser(Integer userId, Pageable pageable);

    // Update
    AddressDto updateAddress(Integer id, AddressDto updatedAddressDto);

    // Delete
    void deleteAddress(Integer id);
}
