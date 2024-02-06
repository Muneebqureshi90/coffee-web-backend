package com.example.coffee.serviceImpl;


import com.example.coffee.dto.CartDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.Cart;
import com.example.coffee.entity.CartItem;
import com.example.coffee.entity.Product;
import com.example.coffee.entity.User;
import com.example.coffee.expections.ProductException;
import com.example.coffee.repository.CartRepository;
import com.example.coffee.repository.ProductDto;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.CartItemService;
import com.example.coffee.services.CartService;
import com.example.coffee.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDto productRepostory;
    @Autowired
    private CartItemService cartItemService;

    @Override
    public CartDto createCart(UserDto userDto) {
        // Map the UserDto to a User entity
        User user = modelMapper.map(userDto, User.class);

        // Create a new cart for the user and save it to the database
        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);

        // Map the Cart entity to a CartDto and return it
        return modelMapper.map(cart, CartDto.class);
    }

//    @Override
//    public CartDto createCart(User user) {
//        // Create a new cart for the user and save it to the database
//        Cart cart = new Cart();
//        cart.setUser(user);
//        cart = cartRepository.save(cart);
//        // Map the Cart entity to a CartDto and return it
//        return modelMapper.map(cart, CartDto.class);
//    }

    @Override
    public String addCartItem(Integer userId, CartDto cartDto) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            // Cart doesn't exist, create a new cart for the user
            cart = new Cart();
            User user = userRepository.getOne(userId);
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // Cart exists, proceed with adding the item
        Product product = productRepostory.findById(cartDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(cartDto.getQuantity());



        BigDecimal price = BigDecimal.valueOf(cartDto.getQuantity())
                .multiply(product.getDiscount_amount());
        cartItem.setPrice(price);

        cart.getCartItems().add(cartItem);

        // Save the updated cart
        cartRepository.save(cart);

        return "Item is added to Cart";
    }





    @Override
    public Cart findUserCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            int totalPrice = 0;
            int totalDiscountedPrice = 0;
            int totalItem = 0;

            // Perform calculations only if the cart is not null
            for (CartItem cartItem : cart.getCartItems()) {
                totalPrice = BigDecimal.valueOf(totalPrice)
                        .add(cartItem.getPrice())
                        .intValue();

                totalDiscountedPrice += cartItem.getDiscountedPrice();
                totalItem += cartItem.getQuantity();
            }

            cart.setTotalPrice(totalPrice);
            cart.setTotalDiscountedPrice(totalDiscountedPrice);
            cart.setTotalItems(totalItem);
            cart.setDiscounte(totalPrice - totalDiscountedPrice);

            return cartRepository.save(cart);
        } else {
            // Handle the case when the cart is not found for the user
            // You might create a new cart for the user or return an error message.
            // Example:
            return null; // or throw an exception or handle the case according to your requirements
        }
    }
}
