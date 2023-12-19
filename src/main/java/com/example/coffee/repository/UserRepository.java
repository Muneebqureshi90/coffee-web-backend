package com.example.coffee.repository;

import com.example.coffee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    void deleteByEmail(String email);
    void deleteByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.otps WHERE u.email = :email")
    Optional<User> findByEmailWithOtps(@Param("email") String email);

//    User findByUserId(Integer userId);

}
