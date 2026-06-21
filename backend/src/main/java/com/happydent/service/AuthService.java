package com.happydent.service;

import com.happydent.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPasswordHash;

    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {
        if (!adminEmail.equalsIgnoreCase(email) || !encoder.matches(password, adminPasswordHash)) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwtUtil.generate(email);
    }
}
