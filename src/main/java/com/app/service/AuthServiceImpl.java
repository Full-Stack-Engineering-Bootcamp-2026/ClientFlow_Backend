package com.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.config.JwtUtil;
import com.app.dto.LoginRequest;
import com.app.dto.LoginResponse;
import com.app.entity.Staff;
import com.app.exception.UnauthorizedException;
import com.app.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil  jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final StaffRepository staffRepository;


    public LoginResponse login(LoginRequest request) {

        Staff user = staffRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid email or password")
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is inactive");
        }

        String role = user.getRole().getName();

        String token = jwtUtil.generateToken(
                user.getEmail(),
                role,
                user.getFullName(),
                user.getOfficialRole()
        );

        return new LoginResponse(token, user.getFullName(), role);
    }
}