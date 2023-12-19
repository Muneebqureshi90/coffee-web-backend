package com.example.coffee.repository;

import com.example.coffee.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
    @Query("SELECT r FROM Rating r WHERE r.product.p_id = :productId AND r.user.id = :userId")
    List<Rating> getAllProductRatingByUser(@Param("productId") Integer productId, @Param("userId") Integer userId);

    @Query("SELECT COUNT(r) > 0 FROM Rating r WHERE r.user.id = :userId AND r.product.p_id = :productId")
    boolean existsByUser_IdAndProduct_Id(@Param("userId") Integer userId, @Param("productId") Integer productId);

}
