package com.demo.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.LoginRequest;
import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.security.JwtUtil;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // 1️⃣ Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2️⃣ Fetch user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        // 4️⃣ Prepare response
        Map<String, Object> response = Map.of(
                "token", token,
                "type", "Bearer",
                "user", Map.of(
                        "id", user.getUserId(),
                        "name", user.getUserName(),
                        "email", user.getEmail(),
                        "roles", user.getUserRoles()
                                .stream()
                                .map(ur -> ur.getRole().getRoleName())
                                .toList()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('BUYER','FARMER','ADMIN')")
    public String hello() {
        return "Hello User";
    }
}
