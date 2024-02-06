package com.example.coffee.controllers;


import com.example.coffee.dto.CartDto;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.Cart;
import com.example.coffee.expections.ProductException;
import com.example.coffee.repository.CartRepository;
import com.example.coffee.repository.OrderRepository;
import com.example.coffee.services.CartService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RequestMapping("/api/cart")
@Tag(name = "Cart Management", description = "Find user cart and add items to the cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create")
    public ResponseEntity<CartDto> createCart(@RequestBody UserDto userDto) {
        // Create a new cart for the user
        CartDto cartDto = cartService.createCart(userDto);

        return new ResponseEntity<CartDto>(cartDto, HttpStatus.CREATED);
    }


    @PutMapping("/addCartItem/{userId}")
    public ResponseEntity addCartItem(
            @RequestParam("userId") Integer userId,
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity) {

        // Create a CartDto and populate it with the request parameters
        CartDto cartDto = new CartDto();
        cartDto.setProductId(productId);
        cartDto.setQuantity(quantity);


        try {
            String result = cartService.addCartItem(userId, cartDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> findUserCart(@PathVariable Integer userId) {
        try {
            Cart cart = cartService.findUserCart(userId);
            if (cart != null) {
                return new ResponseEntity<>(cart, HttpStatus.OK);
            } else {
                // Handle the case when the cart is not found for the user
                // You might create a new cart for the user or return an error message.
                // Example:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle other exceptions if necessary
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
