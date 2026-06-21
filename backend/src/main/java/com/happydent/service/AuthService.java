package com.happydent.service;

import com.happydent.security.JwtUtil;
import com.happydent.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPasswordHash;

    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public String login(String email, String password) {
        if (loginAttemptService.isLocked()) {
            long secs = loginAttemptService.secondsUntilUnlock();
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Too many failed attempts. Try again in " + (secs / 60 + 1) + " minute(s).");
        }
        if (!adminEmail.equalsIgnoreCase(email) || !encoder.matches(password, adminPasswordHash)) {
            loginAttemptService.recordFailure();
            throw new BadCredentialsException("Invalid credentials");
        }
        loginAttemptService.recordSuccess();
        return jwtUtil.generate(email);
    }
}
