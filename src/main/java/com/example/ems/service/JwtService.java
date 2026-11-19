package com.example.ems.service;

import com.example.ems.entity.AdminDetail;

public interface JwtService {

    String generateAccessToken(AdminDetail admin);

    long getAccessTokenLifetimeSeconds();
}
