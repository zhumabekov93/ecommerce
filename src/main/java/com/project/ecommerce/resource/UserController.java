package com.project.ecommerce.resource;

import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.User;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.service.RecommendationService;
import com.project.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("me/history")
    public ResponseEntity<List<Product>> getPurchaseHistory() {
        return ResponseEntity.ok(userService.getPurchaseHistory());
    }

    @GetMapping("/me/recommendations")
    public ResponseEntity<List<Product>> getPersonalizedRecommendations(@RequestParam(defaultValue = "10") int limit) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(recommendationService.getPersonalizedRecommendations(currentUser.getId(), limit));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User userJson) {
        User user = userService.getUserByEmail(userJson.getEmail());
        if(user == null) {
            userService.registerUser(userJson);
            return ResponseEntity.ok(user);
        }else {
            userService.updateUser(user);
            return ResponseEntity.ok(user);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User userJson) {
        userService.deleteUser(userJson.getId());
        return ResponseEntity.ok(userJson);
    }
}
