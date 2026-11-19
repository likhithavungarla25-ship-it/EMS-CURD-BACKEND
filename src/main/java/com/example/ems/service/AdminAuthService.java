
package com.example.ems.service;


import com.example.ems.dto.AuthResponse;
import com.example.ems.dto.LoginRequest;

public interface AdminAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    void logout(String refreshToken);
}
