package com.example.coffee.repository;


import com.example.coffee.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Integer> {


    @Query("SELECT c from Cart c where c.user.id=:userId")
    Cart findByUserId(@Param("userId")Integer userId);

}
