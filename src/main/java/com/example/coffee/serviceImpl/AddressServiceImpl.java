package com.example.coffee.serviceImpl;

import com.example.coffee.config.AppConstants;
import com.example.coffee.dto.AddressDto;
import com.example.coffee.entity.Address;
import com.example.coffee.entity.Order;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.AddressRepository;
import com.example.coffee.repository.OrderRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.AddressService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public AddressDto createAddress(AddressDto addressDto, Integer userId, Integer categoryId) {
        try {
            // Fetch the User entity based on the provided User ID
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

            // Fetch the Order entity based on the provided Order ID
            Order order = orderRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", categoryId.toString()));

            // Set the User and Order entities in the Address entity
            Address address = modelMapper.map(addressDto, Address.class);
            address.setUser(user);
            address.setOrder(order);

            // Save the Address entity
            Address savedAddress = addressRepository.save(address);

            // Map and return the created AddressDto
            return modelMapper.map(savedAddress, AddressDto.class);
        } catch (ResourceNotFoundException e) {
            // Handle the exception and throw or return an appropriate response
            // You may also want to log the exception for debugging purposes
            throw new RuntimeException("Error creating address: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<AddressDto> getAddressById(Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            return Optional.of(addressDto);
        } else {
            // Handle the case where the address is not found, e.g., throw an exception or return an empty Optional
            return Optional.empty();
        }
    }


    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        List<AddressDto> addressDtos = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toList());

        return addressDtos;
    }

    @Override
    public Page<AddressDto> getAddressesByOrder(Integer orderId, Pageable pageable) {
        // Set default values for page number, size, sort by, and sort direction
        int pageNumber = Integer.parseInt(AppConstants.PAGE_NUMBER);
        int pageSize = Integer.parseInt(AppConstants.PAGE_SIZE);
        String sortBy = AppConstants.SORT_BY;
        String sortDir = AppConstants.SORT_DIR;

        // If pageable is not null, use its values
        if (pageable != null) {
            pageNumber = pageable.getPageNumber();
            pageSize = pageable.getPageSize();
            sortBy = pageable.getSort().stream().findFirst().map(order -> order.getProperty()).orElse(sortBy);
            sortDir = pageable.getSort().stream().findFirst().map(order -> order.getDirection().toString()).orElse(sortDir);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId.toString()));
        Page<Address> addressesPage = addressRepository.findByOrder_Id(orderId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));


        return addressesPage.map(address -> modelMapper.map(address, AddressDto.class));
    }
    @Override
    public Page<AddressDto> getAddressesByUser(Integer userId, Pageable pageable) {
        int pageNumber = Integer.parseInt(AppConstants.PAGE_NUMBER);
        int pageSize = Integer.parseInt(AppConstants.PAGE_SIZE);
        String sortBy = AppConstants.SORT_BY;
        String sortDir = AppConstants.SORT_DIR;

        // If pageable is not null, use its values
        if (pageable != null) {
            pageNumber = pageable.getPageNumber();
            pageSize = pageable.getPageSize();
            sortBy = pageable.getSort().stream().findFirst().map(user -> user.getProperty()).orElse(sortBy);
            sortDir = pageable.getSort().stream().findFirst().map(user -> user.getDirection().toString()).orElse(sortDir);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        Page<Address> addressPage = addressRepository.findByUserId(user.getId(), PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));

        return addressPage.map(address -> modelMapper.map(address, AddressDto.class));
    }

    @Override
    public AddressDto updateAddress(Integer id, AddressDto updatedAddressDto) {
        // Find the existing address entity by ID
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id.toString()));

        // Update the existing address entity with the values from the DTO
        existingAddress.setStreetAddress(updatedAddressDto.getStreetAddress());
        existingAddress.setCity(updatedAddressDto.getCity());
        existingAddress.setState(updatedAddressDto.getState());
        existingAddress.setZipCode(updatedAddressDto.getZipCode());

        // Assuming the user and order IDs are present in the DTO, update them as well
        existingAddress.setUser(userRepository.findById(updatedAddressDto.getUserDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", updatedAddressDto.getUserDto().getId().toString())));
        existingAddress.setOrder(orderRepository.findById(updatedAddressDto.getOrderDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", updatedAddressDto.getOrderDto().getId().toString())));

        // Save the updated address entity
        Address updatedAddress = addressRepository.save(existingAddress);

        // Map and return the updated address DTO
        return modelMapper.map(updatedAddress, AddressDto.class);
    }


    @Override
    public void deleteAddress(Integer id) {
        // Check if the address exists
        if (addressRepository.existsById(id)) {
            // Delete the address by ID
            addressRepository.deleteById(id);
        } else {
            // If the address does not exist, throw an exception or handle it as needed
            throw new ResourceNotFoundException("Address", "id", id.toString());
        }
    }

}
