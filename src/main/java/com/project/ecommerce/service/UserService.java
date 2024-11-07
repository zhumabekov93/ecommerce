package com.project.ecommerce.service;

import com.project.ecommerce.exception.UserNotFoundException;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.enums.EventType;
import com.project.ecommerce.model.enums.Role;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final EventService eventService;

    @Transactional
    public User registerUser(User userJson) {
        if (userRepository.existsByEmail(userJson.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(userJson.getEmail());
        user.setPassword(passwordEncoder.encode(userJson.getPassword()));
        user.setName(userJson.getName());
        user.setPreferences(userJson.getPreferences());
        user.setEnabled(true);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        eventService.createUserEvent(savedUser, EventType.CREATE);
        return user;
    }


    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User updateUser(User request) {
        User user = getCurrentUser();

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPreferences() != null) {
            user.setPreferences(request.getPreferences());
        }

        userRepository.save(user);
        eventService.createUserEvent(user, EventType.UPDATE);
        return user;
    }

    @Transactional
    public User updatePreferences(Set<String> preferences) {
        User user = getCurrentUser();
        user.setPreferences(preferences);
        userRepository.save(user);
        eventService.createUserEvent(user, EventType.UPDATE);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<Product> getPurchaseHistory() {
        return productRepository.findAllById(getCurrentUser().getPurchaseHistory());
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
        eventService.createUserEvent(user, EventType.DELETE);
    }
}
