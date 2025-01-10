
package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.User;
import com.gestion.mercantil.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(String username, String firstname, String lastname, String country, String password, String role) {
        Query query = entityManager.createNativeQuery("CALL test.authentication_pkg.register_user(:username, :firstname, :lastname, :country, :password, :role)")
                .setParameter("username", username)
                .setParameter("firstname", firstname)
                .setParameter("lastname", lastname)
                .setParameter("country", country)
                .setParameter("password", password)
                .setParameter("role", role);

        query.executeUpdate();
    }

    public Optional<User> loginUser(String username, String rawPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verificamos la contraseña encriptada
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
