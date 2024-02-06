package com.example.coffee.services;

import com.example.coffee.dto.CartItemDto;
import com.example.coffee.expections.CartItemException;

import java.util.List;

public interface CartItemService {

    List<CartItemDto> getAllCartItems();

    CartItemDto getCartItemById(Integer cartItemId);

    CartItemDto createCartItem(CartItemDto cartItemDto);

    CartItemDto updateCartItem(Integer cartItemId, CartItemDto cartItemDto) throws CartItemException;

    void deleteCartItem(Integer cartItemId) throws CartItemException;
}
