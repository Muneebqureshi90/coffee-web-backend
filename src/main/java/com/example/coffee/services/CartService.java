// CartService.java
package com.example.coffee.services;


import com.example.coffee.dto.CartDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.Cart;
import com.example.coffee.expections.ProductException;

public interface CartService {


//    Cart addItemToCart(Cart cart, Product product, int quantity);
//    Cart updateCartItemQuantity(Cart cart, CartItem cartItem, int newQuantity);
//    Cart removeItemFromCart(Cart cart, CartItem cartItem);
//    double calculateCartTotal(Cart cart);
//    List<CartItem> getCartItems(Cart cart);
//    Cart clearCart(Cart cart);
//    Order checkoutCart(Cart cart);

    CartDto createCart(UserDto userDto);

    String addCartItem(Integer userId, CartDto cartDto) throws ProductException;

    Cart findUserCart(Integer userId);

}
