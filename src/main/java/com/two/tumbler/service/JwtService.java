package com.two.tumbler.service;

import com.two.tumbler.config.JwtConfig;
import com.two.tumbler.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(String email) {
        return jwtUtil.generateToken(email, jwtConfig.getSecret());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return jwtUtil.validateToken(token, userDetails, jwtConfig.getSecret());
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token, jwtConfig.getSecret());
    }
}
