package com.example.coffee.serviceImpl;

import com.example.coffee.dto.CartDto;
import com.example.coffee.dto.CartItemDto;
import com.example.coffee.dto.ProductDto;
import com.example.coffee.entity.CartItem;

import com.example.coffee.expections.CartItemException;
import com.example.coffee.expections.UserException;
import com.example.coffee.repository.CartItemRepository;
import com.example.coffee.services.CartItemService;
import com.example.coffee.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<CartItemDto> getAllCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(cartItem -> modelMapper.map(cartItem, CartItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto getCartItemById(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        return (cartItem != null) ? modelMapper.map(cartItem, CartItemDto.class) : null;
    }

    @Override
    public CartItemDto createCartItem(CartItemDto cartItemDto) {
        CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
        cartItem = cartItemRepository.save(cartItem);
        return modelMapper.map(cartItem, CartItemDto.class);
    }

    @Override
    public CartItemDto updateCartItem(Integer cartItemId, CartItemDto cartItemDto) throws CartItemException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        if (cartItem == null) {
            throw new CartItemException("Cart item not found");
        }

        // Additional validation logic if needed
        cartItem.setProduct(cartItemDto.getProduct());
        cartItem.setPrice(cartItemDto.getPrice());
        cartItem.setDiscountedPrice(cartItemDto.getDiscountedPrice());
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem = cartItemRepository.save(cartItem);


        return modelMapper.map(cartItem, CartItemDto.class);
    }

    @Override
    public void deleteCartItem(Integer cartItemId) throws CartItemException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        if (cartItem == null) {
            throw new CartItemException("Cart item not found");
        }

        // Additional validation logic if needed

        cartItemRepository.delete(cartItem);
    }
}
