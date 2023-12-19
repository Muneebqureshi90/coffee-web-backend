package com.example.coffee.serviceImpl;


import com.example.coffee.entity.RefreshToken;
import com.example.coffee.entity.User;
import com.example.coffee.repository.RefreshRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    public long refreshTokenTime= 79*60*60*1000;
//    public long refreshTokenTime=30;

    @Autowired
    private RefreshRepository refreshRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found")); // Handle the case when the user is not found

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
//                    .expiry(Instant.now().plusMillis(refreshTokenTime))
                    .expiry(Instant.now().plusMillis(refreshTokenTime))
                    .user(user) // Associate the token with the user
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenTime));
        }
        // Update the user's refresh token
        user.setRefreshToken(refreshToken);

        // Save the refresh token along with the updated user association
        return refreshRepository.save(refreshToken);
    }

//    @Override
//    public boolean verifyRefreshToken(String refreshTokenValue) {
//        Optional<RefreshToken> refreshTokenOptional = refreshRepository.findByRefreshToken(refreshTokenValue);
//
//        if (refreshTokenOptional.isPresent() &&
//                refreshTokenOptional.get().getExpiry().isAfter(Instant.now())) {
//            // The token exists and is not expired
//            return true;
//        } else {
//            // The token is either not present or has expired
//            if (refreshTokenOptional.isPresent()) {
//                refreshRepository.delete(refreshTokenOptional.get());
//            }
//            throw new RuntimeException("Refresh Your Token");
//        }
//    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshTokenValue) {
        Optional<RefreshToken> refreshTokenOptional = refreshRepository.findByRefreshToken(refreshTokenValue);

        if (refreshTokenOptional.isPresent() &&
                refreshTokenOptional.get().getExpiry().isAfter(Instant.now())) {
            // The token exists and is not expired
            return refreshTokenOptional.get();
        } else {
            // The token is either not present or has expired
            if (refreshTokenOptional.isPresent()) {
                refreshRepository.delete(refreshTokenOptional.get());
            }
            throw new RuntimeException("Refresh Your Token");
        }

}}


