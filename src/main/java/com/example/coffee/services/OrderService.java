package com.example.coffee.services;

import com.example.coffee.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    List<OrderDto> getAllOrdersByUser(Integer userId);
    OrderDto getOrderById(Integer orderId);
    void updateOrder(OrderDto orderDto);
    void deleteOrder(Integer orderId);
    List<OrderDto> getAllOrders();

}
