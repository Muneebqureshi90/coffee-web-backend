package com.example.coffee.repository;

import com.example.coffee.entity.EmailOtp;
import com.example.coffee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailOtpRepository extends JpaRepository<EmailOtp,Long> {
    List<EmailOtp> findByUser(User user);

    @Modifying
    @Query("DELETE FROM EmailOtp e WHERE e.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);



}
