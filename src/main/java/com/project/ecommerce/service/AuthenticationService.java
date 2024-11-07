package com.project.ecommerce.service;
import com.project.ecommerce.model.CustomUserDetails;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.enums.EventType;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EventService eventService;

    public String register(User userJson) throws Exception {
        User user = userService.registerUser(userJson);
        eventService.createUserEvent(user, EventType.AUTH);
        return jwtService.generateToken(new CustomUserDetails(user));
    }

    public String authenticate(User userJson) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userJson.getEmail(), userJson.getPassword())
        );

        User user = userService.getUserByEmail(userJson.getEmail());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        eventService.createUserEvent(user, EventType.AUTH);
        return jwtService.generateToken(new CustomUserDetails(user));
    }
}