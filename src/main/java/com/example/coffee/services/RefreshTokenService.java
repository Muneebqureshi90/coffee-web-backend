package com.example.coffee.services;


import com.example.coffee.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userName);
    RefreshToken verifyRefreshToken(String refreshToken);

}
