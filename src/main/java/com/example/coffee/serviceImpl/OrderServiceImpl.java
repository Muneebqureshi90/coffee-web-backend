package com.example.coffee.serviceImpl;

import com.example.coffee.dto.AddressDto;
import com.example.coffee.dto.OrderDto;
import com.example.coffee.entity.Address;
import com.example.coffee.entity.Order;
import com.example.coffee.expections.OrderException;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.repository.OrderRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        try {
            // Map the OrderDto to an Order entity
            Order newOrder = modelMapper.map(orderDto, Order.class);

            // Set any other fields or perform additional logic as needed
            newOrder.setCreatedAt(LocalDateTime.now());

            // Save the new order to the database
            orderRepository.save(newOrder);

            // Map the created order entity back to an OrderDto for the response
            return modelMapper.map(newOrder, OrderDto.class);
        } catch (Exception e) {
            // Log or handle the exception as needed
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }



    @Override
    public List<OrderDto> getAllOrdersByUser(Integer userId) {
        try {
            List<Order> orders = orderRepository.findAllByUserId(userId);

            // Map the list of Order entities to a list of OrderDto objects
            List<OrderDto> orderDtos = orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());

            return orderDtos;
        } catch (Exception e) {
            // Handle the exception here, you can log the error or rethrow a custom exception
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);

        }
    }

    @Override
    public OrderDto getOrderById(Integer orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId.toString()));

            // Map the Order entity to an OrderDto object
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);

            return orderDto;
        } catch (ResourceNotFoundException e) {
            // Log the exception for debugging
            logger.error("Order not found: {}", e.getMessage(), e);

            // Rethrow the exception or handle it as appropriate
            throw e;
        } catch (Exception e) {
            // Log the exception for debugging
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);

        }
    }


    @Override
    public void updateOrder(OrderDto orderDto) {
        try {
            // Retrieve the existing order from the database
            Order existingOrder = orderRepository.findById(orderDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderDto.getId().toString()));

            // Update the existing order with data from OrderDto
            existingOrder.setOrderId(orderDto.getOrderId());
            existingOrder.setOrderDate(orderDto.getOrderDate());
            existingOrder.setDeliveryDate(orderDto.getDeliveryDate());
            existingOrder.setShippingAddress(convertToEntity(orderDto.getShippingAddress()));
            existingOrder.setTotalPrice(orderDto.getTotalPrice());
            existingOrder.setTotalDiscountedPrice(orderDto.getTotalDiscountedPrice());
            existingOrder.setOrderStatus(orderDto.getOrderStatus());
            existingOrder.setTotalItems(orderDto.getTotalItems());
            existingOrder.setCreatedAt(orderDto.getCreatedAt());
            // Update other fields as needed

            // Save the updated order
            orderRepository.save(existingOrder);
        } catch (ResourceNotFoundException e) {
            // Log the exception for debugging
            logger.error("Order not found: {}", e.getMessage(), e);

            // Rethrow the exception or handle it as appropriate
            throw e;
        } catch (Exception e) {
            // Log the exception for debugging
            throw new RuntimeException("Error updating order: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteOrder(Integer orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);            // You might want to throw an exception or handle this differently
        }
    }

    @Override
    public List<OrderDto> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            // Map the list of Order entities to a list of OrderDto objects
            return orders.stream()
                    .map(order -> modelMapper.map(order, OrderDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage(), e); // You might want to throw an exception or handle this differently
        }
    }

    public Address convertToEntity(AddressDto addressDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDto, Address.class);
    }

}
