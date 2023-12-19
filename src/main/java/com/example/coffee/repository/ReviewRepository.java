package com.example.coffee.repository;

import com.example.coffee.entity.Rating;
import com.example.coffee.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    @Query("SELECT r FROM Review r WHERE r.product.p_id = :productId AND r.user.id = :userId")
    List<Review> getAllProductReviewByUser(@Param("productId") Integer productId, @Param("userId") Integer userId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.id = :userId AND r.product.p_id = :productId")
    boolean existsByUser1_IdAndProduct_Id(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
