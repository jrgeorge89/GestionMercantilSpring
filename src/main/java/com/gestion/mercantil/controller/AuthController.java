
package com.gestion.mercantil.controller;

import com.gestion.mercantil.dto.AuthResponse;
import com.gestion.mercantil.service.AuthService;
import com.gestion.mercantil.dto.LoginRequest;
import com.gestion.mercantil.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService AuthService;
    
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Handling Login Request: " + request.getUsername()); // Log para Login
        return ResponseEntity.ok(AuthService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("Handling Register Request: " + request.getUsername()); // Log para Registro
        return ResponseEntity.ok(AuthService.register(request));
    }
}

