package com.example.coffee.repository;

import com.example.coffee.entity.UserTwilioOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTwilioOp extends JpaRepository<UserTwilioOtp,Long> {
}
