package com.example.coffee.controllers;

import com.example.coffee.dto.CartItemDto;
import com.example.coffee.expections.CartItemException;
import com.example.coffee.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartitems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/")
    public ResponseEntity<List<CartItemDto>> getAllCartItems() {
        List<CartItemDto> cartItems = cartItemService.getAllCartItems();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> getCartItemById(@PathVariable Integer cartItemId) {
        CartItemDto cartItem = cartItemService.getCartItemById(cartItemId);
        return (cartItem != null) ?
                new ResponseEntity<>(cartItem, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<CartItemDto> createCartItem(@RequestBody CartItemDto cartItemDto) {
        CartItemDto createdCartItem = cartItemService.createCartItem(cartItemDto);
        return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable Integer cartItemId,
            @RequestBody CartItemDto cartItemDto) {
        try {
            CartItemDto updatedCartItem = cartItemService.updateCartItem(cartItemId, cartItemDto);
            return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
        } catch (CartItemException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer cartItemId) {
        try {
            cartItemService.deleteCartItem(cartItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CartItemException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
