package com.project.ecommerce.resource;

import com.project.ecommerce.dto.InteractionDto;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.UserInteraction;
import com.project.ecommerce.model.enums.InteractionType;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.UserInteractionService;
import com.project.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class InteractionController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserInteractionService interactionService;
    private final ProductService productService;

    public InteractionController(UserService userService, UserRepository userRepository, UserInteractionService interactionService, ProductService productService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.interactionService = interactionService;
        this.productService = productService;
    }

    @PostMapping("/interactions")
    public ResponseEntity<UserInteraction> recordInteraction(
            @RequestBody InteractionDto request) {
        User currentUser = userService.getCurrentUser();
        UserInteraction interaction = new UserInteraction();
        interaction.setUserId(currentUser.getId());
        interaction.setProductId(request.getProductId());
        interaction.setType(request.getType());
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setRating(request.getRating());
        switch (request.getType()){
            case PURCHASE -> {
                if (currentUser.getPurchaseHistory()==null){
                    currentUser.setPurchaseHistory(Collections.singletonList(request.getProductId()));
                }else currentUser.getPurchaseHistory().add(request.getProductId());
            }
            case LIKE ->{ if(currentUser.getPreferences()==null) {
                currentUser.setPreferences(Collections.singleton(request.getProductId()));
            }else currentUser.getPreferences().add(request.getProductId());
            }
            case VIEW -> {
                if (currentUser.getViewHistory()==null) {
                    currentUser.setViewHistory(Collections.singletonList(request.getProductId()));
                }else currentUser.getViewHistory().add(request.getProductId());
            }
        }
        userRepository.save(currentUser);
        return ResponseEntity.ok(interactionService.save(interaction));
    }

    @GetMapping("/interactions/user")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(
                interactionService.findByUserId(currentUser.getId(), page, size)
        );
    }

    @GetMapping("/interactions/product/{productId}")
    public ResponseEntity<Map<InteractionType, Long>> getProductInteractionStats(
            @PathVariable String productId) {
        return ResponseEntity.ok(interactionService.getProductStats(productId));
    }
}

