package com.example.coffee.services;

import com.example.coffee.dto.OrderItemDto;

import java.util.List;

public interface OrderItemService {
    OrderItemDto createOrderItem(OrderItemDto orderItemDto, Integer orderId);
    OrderItemDto getOrderItemById(Integer orderItemId);
    void updateOrderItem(OrderItemDto orderItemDto);
    void deleteOrderItem(Integer orderItemId);
    List<OrderItemDto> getAllOrderItemsByOrder(Integer orderId);
    List<OrderItemDto> getAllOrderItems();
}
