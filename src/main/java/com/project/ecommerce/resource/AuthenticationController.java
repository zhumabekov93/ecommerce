package com.project.ecommerce.resource;

import com.project.ecommerce.model.User;
import com.project.ecommerce.service.AuthenticationService;
import com.project.ecommerce.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authenticationService.register(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        try{
        return ResponseEntity.ok(authenticationService.authenticate(user));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }
}
