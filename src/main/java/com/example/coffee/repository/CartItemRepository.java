package com.example.coffee.repository;


import com.example.coffee.entity.Cart;
import com.example.coffee.entity.CartItem;
import com.example.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product ")
    CartItem isCartItemExist(@Param("cart") Cart cart, @Param("product") Product product);


}
