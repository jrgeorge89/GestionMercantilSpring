
package com.gestion.mercantil.service;

import com.gestion.mercantil.dto.AuthResponse;
import com.gestion.mercantil.dto.LoginRequest;
import com.gestion.mercantil.dto.RegisterRequest;
import com.gestion.mercantil.jwt.JwtService;
import com.gestion.mercantil.entity.User;
import com.gestion.mercantil.entity.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Llamamos al procedimiento almacenado para registrar el usuario
        userService.registerUser(
            request.getUsername(), 
            request.getFirstname(), 
            request.getLastname(), 
            request.getCountry(), 
            passwordEncoder.encode(request.getPassword()), 
            Role.USER.name()
        );

        User user = userService.findByUsername(request.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Llamamos a la función almacenada para iniciar sesión
        Optional<User> userOptional = userService.loginUser(request.getUsername(), request.getPassword());
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("Nombre de usuario o contraseña no válidos"));

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
