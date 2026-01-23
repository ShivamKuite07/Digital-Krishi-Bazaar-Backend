package com.demo.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.LoginRequest;
import com.demo.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(
                Map.of(
                    "token", token,
                    "type", "Bearer"
                )
        );
    }
    @GetMapping("/hello")
	@PreAuthorize("hasAnyRole('BUYER','FARMER','ADMIN')")
	public String hello() {
		return "Hello User";
	}
}